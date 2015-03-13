package controllers;

import model.*;
import model.input.InputDTO;
import model.statistics.DoubleQueryRecord;
import org.codehaus.jackson.JsonNode;
import play.Logger;
import play.Play;
import play.libs.Akka;
import play.libs.F;
import play.libs.F.Function;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.Cache;
import services.search.provider.ProviderFactory;
import services.search.provider.SearchProvider;
import services.search.shuffler.DefaultResponseShuffler;
import services.search.shuffler.IResponseShuffler;
import services.statistics.impl.DoubleQueryStatisticsServiceImpl;
import services.statistics.impl.QueryStatisticsServiceImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;


public class Application extends Controller {

    private final static int MIN_UNIQUE_DOMAINS = Play.application().configuration().getInt("ok.double.query.unique.domains", 1);
    private final static int SECOND_REQUEST_NUMBER_FACTOR = Play.application().configuration().getInt("ok.double.query.second.request.factor", 5);
    private final static boolean DOUBLE_QUERY_ENABLED = Play.application().configuration().getBoolean("ok.double.query.enable", Boolean.TRUE);

    @BodyParser.Of(BodyParser.Json.class)
    public static Result search() {
        //TODO precessing parsing exception
        InputDTO inputDTO;
        JsonNode jsonBody = request().body().asJson();
        try {
            inputDTO = Json.fromJson(jsonBody, InputDTO.class);
        } catch (Exception ex) {
            Logger.error("Wrong Query string " + jsonBody.toString());
            return badRequest("Wrong query request: " + jsonBody.toString());
        }
        System.out.println(request().body().asJson());
        System.out.println(inputDTO);
        final SearchRequest request = SearchRequest.fromDTO(inputDTO);

        //send statistics to kafka
        QueryStatisticsServiceImpl.getInstance().insert(request.getQuery());

        if (Cache.isActive() && request.getPage() == 0) {
            Object cachedResponse = Cache.get(request.getQuery());
            if (cachedResponse != null) {
                Logger.debug("Cache hit for query " + request.getQuery());
                JsonNode content = Json.toJson(cachedResponse);
                return ok(content);
            }
        }

        List<F.Promise<? extends SearchResponse>> searchPromises = new ArrayList<>();

        for (final SearchEngineType engineType : request.getSearchEngines()) {

            for (final ContentRequest crequest : request.getContentRequests()) {
                //so far we getting only docs from proxy servers
                if ((engineType == SearchEngineType.GOOGLE || engineType == SearchEngineType.BING) &&
                        (crequest.getSearchType() != SearchType.DOCS || request.getPage() > 0)) {

                    continue;
                }
                final SearchProvider provider = ProviderFactory.getProvider(engineType);
                if (DOUBLE_QUERY_ENABLED && SearchEngineType.LINKY.equals(engineType) && SearchType.DOCS.equals(crequest.getSearchType())) {
                    F.Promise<SearchResponse> respPromise = provider.doSearch(crequest.getSearchType(), request);
                    F.Promise<SearchResponse> resultPromise = respPromise.flatMap(new Function<SearchResponse, F.Promise<SearchResponse>>() {
                        @Override
                        public F.Promise<SearchResponse> apply(final SearchResponse searchResponse) throws Throwable {
                            //Linky search can return resultItems with the same domain,
                            //if there was only 1 unique domain we are making the second search query with bigger size
                            return repeatSearch(searchResponse, request, provider);
                        }
                    });
                    searchPromises.add(resultPromise);
                } else {
                    searchPromises.add(provider.doSearch(crequest.getSearchType(), request));
                }
            }
        }

        //compose promises
        F.Promise<List<SearchResponse>> composedPromise = F.Promise.waitAll(searchPromises);

        F.Promise<Result> result = composedPromise.map(new Function<List<SearchResponse>, Result>() {
            public Result apply(List<SearchResponse> result) {
                IResponseShuffler shuffler = new DefaultResponseShuffler();
                Collection<ResponseItem> shuffledResult = shuffler.shuffle(result);

                if (Cache.isActive()) {
                    Cache.set(request.getQuery(), (Serializable) shuffledResult);
                }

                ResponseContainer resultContainer = new ResponseContainer();
                resultContainer.setItems(shuffledResult);
                resultContainer.buildMetaData();

                Logger.debug("Result from promises has been arrived");
                return ok(Json.toJson(resultContainer));
            }
        });

        return async(result);
    }

    /**
     * repeats search request if there was only 1 UNIQUE domain
     * writes statistics about second and first query into DB
     * with newSize=oldSize*factor
     *
     * @param searchResponse result after first search attempt
     */
    private static F.Promise<SearchResponse> repeatSearch(final SearchResponse searchResponse,
                                                          final SearchRequest request, final SearchProvider provider) {
        SearchMetadata metadata = searchResponse.getMetadata();
        final DoubleQueryRecord qRecord = new DoubleQueryRecord(metadata.getQuery(), metadata.getQueryTime(), metadata.getQuerySize());

        if (searchResponse.getItems().isEmpty() || searchResponse.getItems().size() > MIN_UNIQUE_DOMAINS) {
            Logger.debug("Writing double query record : " + qRecord.getQuery() + "  time: " + qRecord.getFirstQueryTime() + " size: " + qRecord.getFirstQuerySize());
            return Akka.future(new Callable<SearchResponse>() {
                @Override
                public SearchResponse call() throws Exception {
                    return searchResponse;
                }
            });
        }

        int newSize = request.getTotalNumber() * SECOND_REQUEST_NUMBER_FACTOR;
        Logger.debug("The Linky search returned less then " + MIN_UNIQUE_DOMAINS + " unique domains, making second request with size: " + newSize);
        request.setTotalNumber(newSize);

        return provider.doSearch(searchResponse.getSearchType(), request).map(new Function<SearchResponse, SearchResponse>() {
            @Override
            public SearchResponse apply(SearchResponse searchResponse) throws Throwable {
                qRecord.setSecondQueryTime(searchResponse.getMetadata().getQueryTime());
                qRecord.setSecondQuerySize(searchResponse.getMetadata().getQuerySize());

                Logger.debug("Second query:  time " + qRecord.getSecondQueryTime() + " unique domains: " + searchResponse.getItems().size());
                Logger.debug("Writing double query record : " + qRecord.getQuery()
                        + "  time first query: " + qRecord.getFirstQueryTime() + "  first query size: " + qRecord.getFirstQuerySize()
                        + "  time second query: " + qRecord.getSecondQueryTime() + "  second query size: " + qRecord.getSecondQuerySize());

                DoubleQueryStatisticsServiceImpl.getInstance().insert(qRecord);
                return searchResponse;
            }
        }).recover(new Function<Throwable, SearchResponse>() {
            @Override
            public SearchResponse apply(Throwable throwable) throws Throwable {
                Logger.error(throwable.getMessage(), throwable);
                return new SearchResponse();
            }
        });
    }

}
package services.search.provider.impl;

import model.*;
import org.codehaus.jackson.JsonNode;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import play.Logger;
import play.Play;
import play.libs.F;
import play.libs.WS;
import services.search.IPBalancer;
import services.search.after.AfterSearch;
import services.search.after.LinkyAfterSearchGroups;
import services.search.provider.SearchProvider;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class ElasticSearchProvider implements SearchProvider {

    private static final IPBalancer<String> DOC_SERVER_BALANCER;
    private static final Map<SearchType, String> mediaHosts;
    private final AfterSearch afterSearch = new LinkyAfterSearchGroups();
    //TODO query should be improvement.
    private static final String QUERY = "{\n" +
            "   \"query\":{\n" +
            "      \"multi_match\":{\n" +
            "         \"query\":\"%s\",\n" +
            "         \"fields\":[\n" +
            "            \"content\",\n" +
            "            \"title\"\n" +
            "         ]\n" +
            "      }\n" +
            "   },\n" +
            "   \"from\":%s,\n" +
            "   \"size\":%s\n" +
            "}";

    static {
        String docHosts = Play.application().configuration().getString("elastic.docs");
        DOC_SERVER_BALANCER = new IPBalancer<String>();
        DOC_SERVER_BALANCER.init(Arrays.asList(docHosts.split(",")));

        mediaHosts = new HashMap<SearchType, String>();
        mediaHosts.put(SearchType.VIDEO, Play.application().configuration().getString("elastic.video"));
        mediaHosts.put(SearchType.PICTURE, Play.application().configuration().getString("elastic.picture"));
        mediaHosts.put(SearchType.CATALOG, Play.application().configuration().getString("elastic.catalog"));
        mediaHosts.put(SearchType.ADDRESS, Play.application().configuration().getString("elastic.address"));
        mediaHosts.put(SearchType.NEWS, Play.application().configuration().getString("elastic.news"));
    }

    @Override
    public F.Promise<SearchResponse> doSearch(SearchType type, final SearchRequest req) {

        Integer from = req.getPage() * req.getNumber();
        String q = (new Formatter()).format(QUERY, req.getQuery(), from , req.getNumber()).toString();
        String url = getHost(type) + "/" + Schema.ELASTIC_SCHEMA + "/" + type.toString() + "/_search";

        InputStream queryStream = null;
        try {
            queryStream = new ByteArrayInputStream(q.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Logger.error("ElasticSearchProvider: query [" + q + "] can't be uncoded with UTF-8", e);
        }

        final long startTime = System.currentTimeMillis();

        if (Logger.isDebugEnabled()) {
            Logger.debug("ElasticSearch url: " + url);
        }

        return WS.url(url).setTimeout(TIMEOUT)
                .setHeader(HttpHeaders.Names.CONTENT_TYPE, "application/json; charset=utf-8")
                .post(queryStream).map(
                        new F.Function<WS.Response, SearchResponse>() {
                            public SearchResponse apply(WS.Response response) throws Exception {
                                if (Logger.isDebugEnabled()) {
                                    Logger.debug("ElasticSearchProvider: processing response, query [" + req.getQuery() + "]");
                                }

                                JsonNode hits = response.asJson().get("hits").get("hits");
                                SearchMetadata metadata = fillMetadata(response.asJson(), req);
                                SearchResponse searchResponse = parseResponse(hits, req.getQuery(), startTime);
                                searchResponse.setMetadata(metadata);
                                searchResponse = afterSearch.process(searchResponse);
                                Logger.debug("ElasticSearchProvider: items with unique domains: " + searchResponse.getItems().size());
                                return searchResponse;
                            }
                        }
                ).recover(new F.Function<Throwable, SearchResponse>() {
                    @Override
                    public SearchResponse apply(Throwable throwable) throws Throwable {
                        Logger.error(throwable.getMessage(), throwable);
                        return new SearchResponse();
                    }
                });
    }

    private SearchResponse parseResponse(JsonNode hits, String query, long startTime) throws Exception {
        Iterator<JsonNode> it = hits.getElements();
        SearchResponse searchResponse = new SearchResponse();
        try {
            while (it.hasNext()) {
                JsonNode resNode = it.next();
                ResponseItem responseItem = new ResponseItem();
                String type = resNode.get("_type").asText().toUpperCase();
                searchResponse.setSearchType(SearchType.forName(type));

                String score = resNode.get("_score").asText();
                responseItem.setScore(Double.parseDouble(score));

                JsonNode source = resNode.get("_source");
                Iterator<Map.Entry<String, JsonNode>> fieldIt = source.getFields();

                while (fieldIt.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fieldIt.next();
                    if (entry.getKey().equals(Schema.CONTENT)) {
                        responseItem.setSnippet(entry.getValue().asText());

                    } else if (entry.getKey().equals(Schema.TITLE)) {
                        responseItem.setTitle(entry.getValue().asText());

                    } else if (entry.getKey().equals(Schema.URL)) {
                        responseItem.setUrl(entry.getValue().asText());

                    } else if (entry.getKey().equals(Schema.INDEXED_TIME)) {
                        responseItem.setIndexed(Long.parseLong(entry.getValue().asText()));

                    } else {
                        responseItem.addParams(entry.getKey(), entry.getValue().asText());
                    }
                }
                searchResponse.addItem(responseItem);
            }
        } catch (Exception ex) {
            Logger.error("ElasticSearchProvider: Error during parsing search result for query [" + query + "]");
            throw ex;
        }
        if (Logger.isDebugEnabled()) {
            long time = (System.currentTimeMillis() - startTime);
            Logger.debug("ElasticSearchProvider: query [" + query + "], time: " + time + ", found results: " + searchResponse.getItems().size());
        }
        return searchResponse;
    }

    private SearchMetadata fillMetadata(JsonNode response, SearchRequest req) throws NumberFormatException {
        int time = Integer.parseInt(response.get("took").asText());
        int total = Integer.parseInt(response.get("hits").get("total").asText());
        return new SearchMetadata(req.getQuery(), total, time, req.getNumber());
    }

    public static String getHost(SearchType type) {
        if (type == SearchType.DOCS) {
            return DOC_SERVER_BALANCER.getNext();
        } else {
            return mediaHosts.get(type);
        }
    }

}

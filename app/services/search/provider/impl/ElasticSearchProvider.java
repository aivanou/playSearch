package services.search.provider.impl;

import model.Schema;
import model.SearchEngineType;
import model.SearchType;
import model.commands.Command;
import model.request.ContentRequest;
import model.request.InternalContentRequest;
import model.response.ContentResponse;
import model.response.FailedContentResponse;
import model.response.SuccessContentResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import play.Logger;
import play.libs.F;
import play.libs.WS;
import services.search.after.AfterSearch;
import services.search.after.LinkyAfterSearchGroups;
import services.search.provider.SearchProvider;
import util.SearchConfiguration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElasticSearchProvider implements SearchProvider {

    private final SearchEngineType type;
    //TODO add apter srearch to the workflow
    private final AfterSearch afterSearch = new LinkyAfterSearchGroups();

    private List<Command> commands;

    public ElasticSearchProvider(SearchEngineType type) {
        this.type = type;
        this.commands = new ArrayList(SearchConfiguration.getInstance().getCommandsByEngine(type));
    }

    @Override
    public F.Promise<ContentResponse> doSearch(final ContentRequest req) {

        if (!(req instanceof InternalContentRequest))
            throw new ClassCastException("Elastic search accepts only instances of internal content requests");
        final InternalContentRequest ireq = (InternalContentRequest) req;
        Command command = getCommandByName("search");
        Map<String, String> parameters = fillParameters(ireq);
        String q = command.fill(parameters, ireq.getSearchType().getSearchFields());
        String url = buildUrl(ireq);
        if (Logger.isDebugEnabled()) {
            Logger.debug("ElasticSearchProvider: Sending request to : " + url);
            Logger.debug("Elastic search query: " + q);
        }
        InputStream queryStream = null;
        try {
            queryStream = new ByteArrayInputStream(q.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Logger.error("ElasticSearchProvider: query [" + q + "] can't be uncoded with UTF-8", e);
        }

        if (Logger.isDebugEnabled()) {
            Logger.debug("ElasticSearch url: " + url);
        }

        return WS.url(url).setTimeout(TIMEOUT)
                .setHeader(HttpHeaders.Names.CONTENT_TYPE, "application/json; charset=utf-8")
                .post(queryStream).map(
                        new F.Function<WS.Response, ContentResponse>() {
                            public ContentResponse apply(WS.Response response) throws Exception {
                                if (Logger.isDebugEnabled()) {
                                    Logger.debug("ElasticSearchProvider: processing response, query [" + ireq.getQuery() + "]");
                                    Logger.debug("Elastic search response : " + response.asJson());
                                }
                                SuccessContentResponse contentResponse = SuccessContentResponse.buildFromElasticJson(response.asJson(), ireq.getQuery(), ireq.getSearchType().getName());
                                if (Logger.isDebugEnabled()) {
                                    Logger.debug("ElasticSearchProvider: items with unique domains: " + contentResponse.getItems().size());
                                }
                                return contentResponse;
                            }
                        }
                ).recover(new F.Function<Throwable, ContentResponse>() {
                    @Override
                    public ContentResponse apply(Throwable throwable) throws Throwable {
                        Logger.error(throwable.getMessage(), throwable);
                        return new FailedContentResponse(ireq.getSearchType().getName(), throwable);
                    }
                });
    }

    private Map<String, String> fillParameters(InternalContentRequest ireq) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("from", ireq.getFrom() + "");
        parameters.put("size", ireq.getNumber() + "");
        parameters.put("query", ireq.getQuery());
        return parameters;
    }

    private String buildUrl(InternalContentRequest ireq) {
        return getHost(ireq.getSearchType(), type) + "/" + Schema.ELASTIC_SCHEMA + "/" + ireq.getSearchType().getName() + "/_search";
    }

    public Command getCommandByName(String name) {
        for (Command c : commands)
            if (c.getName().endsWith(name))
                return c;
        return null;
    }

    public String getHost(SearchType type, SearchEngineType engine) {
        return type.getHosts().get(engine).iterator().next();
    }
}

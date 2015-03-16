package services.search.provider.impl;

import model.Schema;
import model.SearchEngineType;
import model.SearchType;
import model.commands.Command;
import model.request.ContentRequest;
import model.request.InternalContentRequest;
import model.response.ContentResponse;
import model.response.FailedContentResponse;
import model.response.ResponseItem;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class ElasticSearchProvider implements SearchProvider {

    private final SearchEngineType type;
    private final AfterSearch afterSearch = new LinkyAfterSearchGroups();

    private List<Command> commands;

    public ElasticSearchProvider(SearchEngineType type) {
        this.type = type;
        this.commands = new ArrayList(SearchConfiguration.getInstance().getCommandsByEngine(type));
    }

    @Override
    public F.Promise<ContentResponse> doSearch(final ContentRequest req) {

        if (req instanceof InternalContentRequest)
            throw new ClassCastException("Elastic search accepts only instances of internal content requests");
        final InternalContentRequest ireq = (InternalContentRequest) req;
        Command command = getCommandByName("search");
        Map<String, String> parameters = new HashMap<>();
        parameters.put("from", req.getFrom() + "");
        parameters.put("size", req.getNumber() + "");
        parameters.put("query", req.getQuery());
        String q = command.fill(parameters, ireq.getSearchType().getSearchFields());
//        String q = (new Formatter()).format(QUERY, req.getQuery(), req.getFrom(), req.getNumber()).toString();
        String url = getHost(ireq.getSearchType(), type) + "/" + Schema.ELASTIC_SCHEMA + "/" + ireq.getSearchType().getName() + "/_search";
//        String url = "http://localhost:9200/documents/_search?pretty";
        InputStream queryStream = null;
        Logger.debug("ElasticSearchProvider: Sending request to : " + url);
        Logger.debug("Elastic search query: " + q);
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
                                }
                                System.out.println(response.asJson());
                                SuccessContentResponse contentResponse = SuccessContentResponse.buildFromElasticJson(response.asJson(), ireq.getQuery(), ireq.getSearchType().getName());
//                                contentResponse = afterSearch.process(contentResponse);
                                Logger.debug("ElasticSearchProvider: items with unique domains: " + contentResponse.getItems().size());
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

    public Command getCommandByName(String name) {
        for (Command c : commands)
            if (c.getName().endsWith(name))
                return c;
        return null;
    }

    public String getHost(SearchType type, SearchEngineType engine) {
        return type.getHosts().get(engine).iterator().next();
//        return ipBanancer.getHost();
    }


    public static void main(String[] args) throws IOException {

//        String query = String.format(q, "plbvryd", 0, 20);
//        InputStream queryStream = new ByteArrayInputStream(query.getBytes("UTF-8"));
//        String surl = "http://localhost:9200/documents/_search?pretty";
//        WS.Response response = WS.url(surl).setTimeout(2000)
//                .setHeader(HttpHeaders.Names.CONTENT_TYPE, "application/json; charset=utf-8")
//                .post(queryStream).get();
//        System.out.println(response.asJson());
//        String url = "http://localhost:9200/linky/document";
//        new ElasticSearchProvider().addRandomDocuments(url, 10);
        List<String> searchFields = Arrays.asList("title", "content");
        Map<SearchEngineType, List<String>> hosts = new HashMap<>();
        hosts.put(new SearchEngineType("elastic"), Arrays.asList("http://localhost:9200"));
        SearchType stype = new SearchType("document", searchFields, hosts);
        SuccessContentResponse r = (SuccessContentResponse) new ElasticSearchProvider(new SearchEngineType("elastic")).doSearch(new InternalContentRequest("lvmcp", stype, 20, 0)).get();
        System.out.println(r.getItems().size());
        for (ResponseItem item : r.getItems()) {
            System.out.println(item);
        }
        System.out.println(r.toJson());
    }

    public void addRandomDocuments(String url, int number) {
        for (int i = 0; i < number; ++i) {
            try {
                System.out.println(addRandomDocument(url).get().getBody());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public F.Promise<WS.Response> addRandomDocument(String url) throws UnsupportedEncodingException {
        String title = randomText(2, 4, 5, 8);
        String text = randomText(8, 20, 3, 8);
        String docUrl = "http://test.com/" + randomString(5, 10);
        return addDocument(title, text, docUrl, System.nanoTime(), url);
    }

    public F.Promise<WS.Response> addDocument(String title, String content, String documentUrl, long indexed, String url) throws UnsupportedEncodingException {
        String q = "{\n" +
                "        \"content\":\"%s\",\n" +
                "        \"title\":\"%s\",\n" +
                "        \"url\":\"%s\",\n" +
                "        \"indexed\":%s\n" +
                "}";
        String query = String.format(q, content, title, documentUrl, indexed);
        return execute(url, query, 3000);
    }

    public F.Promise<WS.Response> execute(String url, String query, int timeout) throws UnsupportedEncodingException {
        InputStream queryStream = new ByteArrayInputStream(query.getBytes("UTF-8"));
        return WS.url(url).setTimeout(timeout)
                .setHeader(HttpHeaders.Names.CONTENT_TYPE, "application/json; charset=utf-8")
                .post(queryStream);
    }

    public String randomText(int min, int max, int minStr, int maxStr) {
        StringBuilder text = new StringBuilder();
        int len = new Random().nextInt(max - min) + min;
        for (int i = 0; i < len; ++i) {
            text.append(randomString(minStr, maxStr));
            text.append(" ");
        }
        return text.toString();
    }

    public String randomString(int min, int max) {
        StringBuilder str = new StringBuilder();
        int len = new Random().nextInt(max - min) + min;
        for (int i = 0; i < len; ++i) {
            str.append(randomCharacter());
        }
        return str.toString();
    }

    public char randomCharacter() {
        Random r = new Random();
        return (char) (r.nextInt(122 - 97) + 97);
    }

}

package services.search.provider.impl;

import com.ning.http.client.Realm;
import model.SearchEngineType;
import model.SearchType;
import model.request.ContentRequest;
import model.request.SearchRequest;
import model.response.ContentResponse;
import model.response.SearchResponse;
import play.Play;
import play.libs.F;
import play.libs.WS;
import services.search.provider.SearchProvider;
import services.search.provider.impl.supervisor.ProxySupervisor;

/**
 * This Provider getting data from Google or Bing via proxy API provided by seo.promo.co.th.
 * Using BASIC authentication.
 */
public class ProxyAPIProvider implements SearchProvider {
    private static final String GOOGLE_URL = "http://seo.promo.co.th/check/google_search";
    private static final String BING_URL = "http://seo.promo.co.th/check/bing_search";
    private static final String PROXY_LOGIN = Play.application().configuration().getString("proxy.login");
    private static final String PROXY_PASS = Play.application().configuration().getString("proxy.password");
    private static final String QUERY_PARAM = "query";
    private static final String LANG_PARAM = "lang";
    private static final String COUNTRY_PARAM = "country";
    private static final String PAGE_NUM_PARAM = "page_num";
    private static final String RESULT_LIMIT = "limit";
    private static final String RESULT_NUMBERS = Play.application().configuration().getString("proxy.result.limit");
    private SearchEngineType engineType;
    private ProxySupervisor supervisor;

    public ProxyAPIProvider(SearchEngineType engineType) {
        this.engineType = engineType;
    }

    @Override
    public F.Promise<ContentResponse> doSearch(final ContentRequest req) {
//        final long startTime = System.currentTimeMillis();
//        if (supervisor == null || (supervisor != null && supervisor.isProxyAvailable())) {
//            return get(req.getQuery(), req.getPage()).map(
//                    new F.Function<WS.Response, SearchResponse>() {
//                        public SearchResponse apply(WS.Response response) throws Exception {
//                            if (Logger.isDebugEnabled()) {
//                                Logger.debug("ProxyProvider: processing response, query [" + req.getQuery() + "]");
//                            }
//
//                            SearchResponse searchResponse = null;
////                            searchResponse.setSearchType(type);
//
//                            try {
//                                Iterator<JsonNode> it = response.asJson().get("links").iterator();
//                                while (it.hasNext()) {
//                                    ResponseItem responseItem = new ResponseItem();
//                                    JsonNode item = it.next();
//                                    responseItem.setUrl(item.get("url").asText());
//                                    responseItem.setSnippet(item.get("description").asText());
//                                    responseItem.setTitle(item.get("title").asText());
////                                    searchResponse.addItem(responseItem);
//                                }
//                            } catch (Exception ex) {
//                                Logger.error("ProxyProvider: Error during parsing search result for query [" + req.getQuery() + "]");
//                                throw ex;
//                            }
//
//                            if (Logger.isDebugEnabled()) {
//                                Logger.debug("ProxyProvider: query[" + req.getQuery() + "] time: " + (System.currentTimeMillis() - startTime));
//                            }
//                            return searchResponse;
//                        }
//                    }
//            ).recover(new F.Function<Throwable, SearchResponse>() {
//                @Override
//                public SearchResponse apply(Throwable throwable) throws Throwable {
//                    if (throwable instanceof TimeoutException) {
//                        String msg = String.format("Timeout on %s proxy API exceeded", engineType);
//                        Logger.debug(msg);
//                        if (supervisor != null) {
//                            supervisor.fail(msg);
//                        }
//                    }
//                    Logger.error("Failed to get proxy API result because of: " + throwable.getLocalizedMessage());
////                    return new SearchResponse(type);
//                return null;
//                }
//            });
//        } else {
//            Logger.debug(String.format("Proxy API %s is unavailable, using key API instead", engineType));
//            return ProviderFactory.getKeyProvider(engineType).doSearch(type, req);
//        }
        return null;
    }

    public F.Promise<WS.Response> get(String query, int page) {
        return WS.url(resolveUrl()).setTimeout(TIMEOUT)
                .setAuth(PROXY_LOGIN, PROXY_PASS, Realm.AuthScheme.BASIC)
                .setQueryParameter(QUERY_PARAM, query)
                .setQueryParameter(LANG_PARAM, "th")
                .setQueryParameter(COUNTRY_PARAM, "TH")
                .setQueryParameter(PAGE_NUM_PARAM, String.valueOf(page))
                .setQueryParameter(RESULT_LIMIT, RESULT_NUMBERS)
                .get();
    }

    private String resolveUrl() {
        if (engineType == SearchEngineType.BING) {
            return BING_URL;
        }
        return GOOGLE_URL;
    }

    public void setSupervisor(ProxySupervisor supervisor) {
        this.supervisor = supervisor;
    }
}

package services.search.provider.impl;

import asia.linky.killer.common.SearchEngine;
import model.SearchEngineType;
import model.request.ContentRequest;
import model.response.ContentResponse;
import play.libs.F;
import services.search.provider.SearchProvider;
import services.search.provider.impl.supervisor.ProxySupervisor;

public class ProxyProvider implements SearchProvider {
    private SearchEngineType engineType;
    private SearchEngine proxySearchEngine;
    private ProxySupervisor supervisor;

    public ProxyProvider(SearchEngineType searchEngineType) {
        this.engineType = searchEngineType;
        switch (searchEngineType) {
            case BING: {
                proxySearchEngine = SearchEngine.BING;
                break;
            }
            case GOOGLE: {
                proxySearchEngine = SearchEngine.GOOGLE;
                break;
            }
            default: {
                throw new IllegalArgumentException("Bad search engine type for proxy: " + searchEngineType);
            }
        }
    }

    public void setSupervisor(ProxySupervisor supervisor) {
        this.supervisor = supervisor;
    }

    @Override
    public F.Promise<ContentResponse> doSearch(final ContentRequest req) {
//        if (supervisor == null || (supervisor != null && supervisor.isProxyAvailable())) {
//            SearchTask task = new ProxySearchTask(req.getQuery(), proxySearchEngine, TIMEOUT);
//            return task.doSearch().map(new F.Function<WS.Response, SearchResponse>() {
//                @Override
//                public SearchResponse apply(WS.Response response) throws Throwable {
//                    SearchResponse searchResponse =null;
//                    if (response.getStatus() == Http.Status.OK) {
//                        String decompress = PlainSearchTask.decompress(response.getBodyAsStream());
//                        Collection<ResponseItem> items = ParserFactory.newInstance(engineType).parse(decompress, req.getQuery());
//                        if (items != null) {
////                            searchResponse.addItems(items);
//                        }
//                    } else {
//                        Logger.error("Proxy provider got response code " + response.getStatus() + " " + response.getStatusText() + " URI=" + response.getUri());
//                    }
//                    return searchResponse;
//                }
//            }).recover(new F.Function<Throwable, SearchResponse>() {
//                @Override
//                public SearchResponse apply(Throwable throwable) throws Throwable {
//                    if (throwable instanceof TimeoutException) {
//                        String msg = String.format("Timeout on %s proxy exceeded", engineType);
//                        Logger.debug(msg);
//                        if (supervisor != null) {
//                            supervisor.fail(msg);
//                        }
//                    }
//                    Logger.error("Failed to get proxy result because of: " + throwable.getLocalizedMessage());
////                    return new SearchResponse(type);
//                return null;
//                }
//            });
//        } else {
//            Logger.debug(String.format("Proxy %s is unavailable, using key API instead", engineType));
//            return ProviderFactory.getKeyProvider(engineType).doSearch(type, req);
//        }
        return null;
    }
}

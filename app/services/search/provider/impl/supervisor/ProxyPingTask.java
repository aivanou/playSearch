package services.search.provider.impl.supervisor;

import asia.linky.killer.common.SearchEngine;
import model.SearchEngineType;
import play.mvc.Http;
import services.searchtask.ProxySearchTask;

import static services.search.provider.SearchProvider.TIMEOUT;

/**
 * ProxyPingTask ...
 *
 * @author vadim
 * @date 1/9/13
 */
public class ProxyPingTask extends PingTask {

    private final SearchEngine engine;

    public ProxyPingTask(SearchEngineType engineType) {
        super(engineType);
        switch (super.engineType) {
            case GOOGLE: {
                engine = SearchEngine.GOOGLE;
                break;
            }
            case BING: {
                engine = SearchEngine.BING;
                break;
            }
            default: {
                throw new IllegalArgumentException(String.format("Engine type %s is not supported by proxy ping task", engineType));
            }
        }
    }

    @Override
    public Boolean call() {
        try {
            return new ProxySearchTask(PING_QUERY, engine, TIMEOUT).doSearch().get().getStatus() == Http.Status.OK;
        } catch (Throwable e) {
            return Boolean.FALSE;
        }
    }

    public static ProxyPingTask forType(SearchEngineType type) {
        switch (type) {
            case GOOGLE: {
                return new ProxyPingTask(SearchEngineType.GOOGLE);
            }
            case BING: {
                return new ProxyPingTask(SearchEngineType.BING);
            }
            default: {
                throw new IllegalArgumentException("Invalid engine type: " + type);
            }
        }
    }
}

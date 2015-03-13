package services.search.provider.impl.supervisor;

import model.SearchEngineType;
import play.mvc.Http;
import services.search.provider.impl.ProxyAPIProvider;

/**
 * ProxyApiPingTask ...
 *
 * @author vadim
 * @date 1/9/13
 */
public class ProxyApiPingTask extends PingTask {

    private final ProxyAPIProvider provider;

    public ProxyApiPingTask(SearchEngineType engineType) {
        super(engineType);
        provider = new ProxyAPIProvider(super.engineType);
    }

    @Override
    public Boolean call() throws Exception {
        try {
            return provider.get(PING_QUERY, 1).get().getStatus() == Http.Status.OK;
        } catch (Throwable e) {
            return Boolean.FALSE;
        }
    }

    public static ProxyApiPingTask forType(SearchEngineType type) {
        switch (type) {
            case GOOGLE: {
                return new ProxyApiPingTask(SearchEngineType.GOOGLE);
            }
            case BING: {
                return new ProxyApiPingTask(SearchEngineType.BING);
            }
            default: {
                throw new IllegalArgumentException("Invalid engine type: " + type);
            }
        }
    }
}

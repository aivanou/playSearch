package services.search.provider;

import com.akavita.metasearch.keystorage.service.KeyProvider;
import model.SearchEngineType;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import play.Play;
import services.search.provider.impl.*;
import services.search.provider.impl.supervisor.MailService;
import services.search.provider.impl.supervisor.ProxyApiPingTask;
import services.search.provider.impl.supervisor.ProxyPingTask;
import services.search.provider.impl.supervisor.ProxySupervisor;
import services.spring.SpringContextLoader;

public class ProviderFactory {
    private final static EngineFlag flag = EngineFlag.forName(Play.application().configuration().getString("ok.engine", EngineFlag.API.toString()));

    private static final GoogleApiSearchProvider GOOGLE_KEY_PROVIDER;
    private static final BingApiSearchProvider BING_KEY_PROVIDER;
    private static final ElasticSearchProvider LINKY_PROVIDER = new ElasticSearchProvider();
    private static final ProxyProvider GOOGLE_PROXY_PROVIDER, BING_PROXY_PROVIDER;
    private static final ProxyAPIProvider GOOGLE_API_PROVIDER, BING_API_PROVIDER;

    static {
        ClassPathXmlApplicationContext context = SpringContextLoader.getContext("keyProviderBeans.xml");
        GOOGLE_KEY_PROVIDER = new GoogleApiSearchProvider(context.getBean("googleMysqlKeyProvider", KeyProvider.class));
        BING_KEY_PROVIDER = new BingApiSearchProvider(context.getBean("bingMysqlKeyProvider", KeyProvider.class));
        MailService mailer = MailService.defaultInstance();
        int checkDelay = Play.application().configuration().getInt("proxy.supervisor.delay");
        int failsLimit = Play.application().configuration().getInt("proxy.supervisor.limit");
        String email = Play.application().configuration().getString("proxy.supervisor.email").replaceAll("\"", " ");
        ProxySupervisor googleApiSupervosor = new ProxySupervisor(mailer, ProxyApiPingTask.forType(SearchEngineType.GOOGLE), checkDelay, failsLimit, email);
        ProxySupervisor bingApiSupervosor = new ProxySupervisor(mailer, ProxyApiPingTask.forType(SearchEngineType.BING), checkDelay, failsLimit, email);
        ProxySupervisor googleProxySupervosor = new ProxySupervisor(mailer, ProxyPingTask.forType(SearchEngineType.GOOGLE), checkDelay, failsLimit, email);
        ProxySupervisor bingProxySupervosor = new ProxySupervisor(mailer, ProxyPingTask.forType(SearchEngineType.BING), checkDelay, failsLimit, email);
        GOOGLE_API_PROVIDER = new ProxyAPIProvider(SearchEngineType.GOOGLE);
        GOOGLE_API_PROVIDER.setSupervisor(googleApiSupervosor);
        GOOGLE_PROXY_PROVIDER = new ProxyProvider(SearchEngineType.GOOGLE);
        GOOGLE_PROXY_PROVIDER.setSupervisor(googleProxySupervosor);

        BING_API_PROVIDER = new ProxyAPIProvider(SearchEngineType.BING);
        BING_API_PROVIDER.setSupervisor(bingApiSupervosor);
        BING_PROXY_PROVIDER = new ProxyProvider(SearchEngineType.BING);
        BING_PROXY_PROVIDER.setSupervisor(bingProxySupervosor);

    }

    public static SearchProvider getProvider(SearchEngineType engineType) {
        switch (engineType) {
            case LINKY: {
                return LINKY_PROVIDER;
            }
            case GOOGLE:
            case BING: {
                switch (flag) {
                    case API: {
                        return engineType == SearchEngineType.GOOGLE ? GOOGLE_API_PROVIDER : BING_API_PROVIDER;
                    }
                    case KEY: {
                        return engineType == SearchEngineType.GOOGLE ? GOOGLE_KEY_PROVIDER : BING_KEY_PROVIDER;
                    }
                    case PROXY: {
                        return engineType == SearchEngineType.GOOGLE ? GOOGLE_PROXY_PROVIDER : BING_PROXY_PROVIDER;
                    }
                    default: {
                        throw new IllegalArgumentException(flag + " engine is not supported");
                    }
                }
            }
            default: {
                throw new IllegalArgumentException(engineType + " search engine type is not supported");
            }
        }
    }

    public static ApiSearchProvider getKeyProvider(SearchEngineType type) {
        switch (type) {
            case BING: {
                return BING_KEY_PROVIDER;
            }
            case GOOGLE: {
                return GOOGLE_KEY_PROVIDER;
            }
            default: {
                throw new IllegalArgumentException("Invalid search engine type for key api: " + type);
            }
        }
    }

    private static enum EngineFlag {
        API("api"),
        KEY("key"),
        PROXY("proxy");
        private final String name;

        private EngineFlag(String name) {
            this.name = name;
        }

        public static EngineFlag forName(String name) {
            for (EngineFlag f : values()) {
                if (f.name.equals(name)) {
                    return f;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}

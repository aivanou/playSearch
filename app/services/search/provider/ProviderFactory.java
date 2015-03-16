package services.search.provider;

import model.SearchEngineType;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.spring.SpringContextLoader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProviderFactory {
    private static final Map<SearchEngineType, SearchProvider> providers = new ConcurrentHashMap<>();

    static {
        ClassPathXmlApplicationContext context = SpringContextLoader.getContext("keyProviderBeans.xml");
        SearchProvider elasticProvider = (SearchProvider) context.getBean("elasticSearchProvider");
        providers.put(new SearchEngineType("elastic"), elasticProvider);
    }

    public static SearchProvider getProvider(SearchEngineType engineType) {
        return providers.get(engineType);
    }

    public static Class<? extends SearchProvider> getProviderClass(SearchEngineType type) {
        if (providers.containsKey(type))
            return providers.get(type).getClass();
        return null;
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

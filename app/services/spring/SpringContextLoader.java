package services.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SpringContextLoader {

    private static final Map<String, ClassPathXmlApplicationContext> contexts = new ConcurrentHashMap<>();

    private SpringContextLoader() {

    }

    public static ClassPathXmlApplicationContext getContext(String contextFilePath) {
        ClassPathXmlApplicationContext context = contexts.get(contextFilePath);
        if (context == null) {
            contexts.put(contextFilePath, new ClassPathXmlApplicationContext(contextFilePath));
            return contexts.get(contextFilePath);
        }

        return context;
    }
}

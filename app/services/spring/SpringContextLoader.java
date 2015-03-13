package services.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;

public final class SpringContextLoader {

    private static Map<String, ClassPathXmlApplicationContext> contexts = new HashMap<String, ClassPathXmlApplicationContext>();

    private SpringContextLoader() {

    }

    public static synchronized ClassPathXmlApplicationContext getContext(String contextFilePath) {
        ClassPathXmlApplicationContext context = contexts.get(contextFilePath);
        if (context == null) {
            contexts.put(contextFilePath, new ClassPathXmlApplicationContext(contextFilePath));
            return contexts.get(contextFilePath);
        }

        return context;
    }
}

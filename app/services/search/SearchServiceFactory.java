package services.search;


import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.spring.SpringContextLoader;

/**
 * Uses spring to instantiate the appropriate
 *
 * @see services.search.SearchService
 */

public class SearchServiceFactory {

    private static SearchService instance = null;
    private static final Object lock = new Object();
    private static ClassPathXmlApplicationContext context;

    public static SearchService getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    context = SpringContextLoader.getContext("keyProviderBeans.xml");
                    instance = (SearchService) context.getBean("searchService");
                }
            }
        }
        return instance;
    }

}

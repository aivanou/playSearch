package services.cache.impl;

import model.request.SearchRequest;
import model.response.SearchResponse;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import scala.concurrent.ExecutionContext;
import services.cache.Cache;
import services.search.SearchService;
import services.spring.SpringContextLoader;


public class RedisCache extends Cache {

    private static RedisCache instance = null;
    private static Object lock = new Object();

    public static RedisCache getInstance(ExecutionContext exContext, JedisPoolConfig config, String host, int port) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new RedisCache(exContext, config, host, port);
                }
            }
        }
        return instance;
    }

    private final JedisPool pool;

    private RedisCache(ExecutionContext exContext, JedisPoolConfig config, String host, int port) {
        super(exContext);
        this.pool = new JedisPool(config, host, port);
    }

    @Override
    protected void setBy(String key, String query, int ttl) {
        try (Jedis jedis = pool.getResource()) {
            jedis.set(key, query);
            jedis.expire(key, ttl);
        }
    }

    @Override
    protected String getBy(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.get(key);
        }
    }


    @Override
    public void stop() {
        synchronized (instance) {
            pool.destroy();
        }
    }

    public static void main(String[] args) {
//        JedisPoolConfig conf = new JedisPoolConfig();
//        conf.setMaxTotal(100);
//        conf.setMaxWaitMillis(5000);
//        Cache cache = RedisCache.getInstance(null, conf, "localhost", 6379);
//        Future<String> f = cache.get("enenen");
//        f.onSuccess(new OnSuccess<String>() {
//            @Override
//            public void onSuccess(String result) throws Throwable {
//                System.out.println("YES");
//                System.out.println(result);
//            }
//        }, cache.getExecutionContext());
//        f.onFailure(new OnFailure() {
//            @Override
//            public void onFailure(Throwable failure) throws Throwable {
//                System.out.println("NOO");
//                System.out.println(failure.getErrorMessage());
//                failure.printStackTrace();
//            }
//
//        }, cache.getExecutionContext());
//        cache.stop();
        ClassPathXmlApplicationContext context = SpringContextLoader.getContext("keyProviderBeans.xml");
        SearchService<SearchRequest, SearchResponse> searchService = (SearchService<SearchRequest, SearchResponse>) context.getBean("searchService");
    }

}

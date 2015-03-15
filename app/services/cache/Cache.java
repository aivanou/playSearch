package services.cache;


import akka.dispatch.Futures;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;

import java.util.concurrent.Callable;

public abstract class Cache {

    private ExecutionContext exContext;

    public Cache(ExecutionContext exContext) {
        this.exContext = exContext;
    }

    public Future<Void> set(final String key, final String query, final int ttl) {
        Future<Void> future = Futures.future(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    setBy(key, query, ttl);
                } catch (Exception ex) {
                    throw ex;
                }
                return null;
            }
        }, exContext);
        return future;
    }

    public Future<String> get(final String key) {
        Future<String> future = Futures.future(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return getBy(key);
            }
        }, exContext);
        return future;
    }


    public ExecutionContext getExecutionContext() {
        return this.exContext;
    }

    protected abstract void setBy(String key, String query, int ttl);

    protected abstract String getBy(String key);

    public abstract void stop();


}

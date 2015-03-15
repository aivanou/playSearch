package services.statistics.impl;

import scala.concurrent.Future;
import scala.concurrent.Promise;
import services.statistics.StatService;

import java.util.Collection;

public class FakeStatisticsService<T> implements StatService<T> {
    @Override
    public void send(Collection<T> objects) {

    }

    @Override
    public Future<String> asyncSend(Collection<T> objects) {
        Promise<String> p = new scala.concurrent.impl.Promise.DefaultPromise<>();
        return p.future();
    }
}

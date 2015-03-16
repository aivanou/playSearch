package services.statistics.impl;

import model.Writable;
import scala.concurrent.Promise;
import services.statistics.StatService;

import java.util.Collection;

public class FakeStatisticsService<T extends Writable> implements StatService<T> {
    @Override
    public void send(Collection<T> objects) {

    }

    @Override
    public void asyncSend(Collection<T> objects) {
        Promise<String> p = new scala.concurrent.impl.Promise.DefaultPromise<>();
    }
}

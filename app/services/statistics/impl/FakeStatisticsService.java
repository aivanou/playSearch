package services.statistics.impl;

import model.Writable;
import services.statistics.StatService;

import java.util.Collection;

/**
 * Fake query statistics writer
 *
 * @param <T>
 */
public class FakeStatisticsService<T extends Writable> implements StatService<T> {
    @Override
    public void send(Collection<T> objects) {
    }

    @Override
    public void asyncSend(Collection<T> objects) {
        //write somewhere?
    }
}

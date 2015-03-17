package services.statistics;

import model.Writable;

import java.util.Collection;

/**
 * Represents the service for sending messages(currently statistics)
 *
 * @param <T> Class that supports @see model.Writable
 */
public interface StatService<T extends Writable> {

    public void send(Collection<T> objects);

    public void asyncSend(Collection<T> objects);

}

package services.statistics;

import model.Writable;

import java.util.Collection;

public interface StatService<T extends Writable> {

    public void send(Collection<T> objects);

    public void asyncSend(Collection<T> objects);

}

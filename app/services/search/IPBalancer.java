package services.search;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * The round robin algorithm represented by a queue
 * polls the first element and adds it to the tail,
 * if heavy loaded, the hosts can be gived out of order
 *
 * @param <T>
 */
// TODO: keep track of the dead hosts, if dead host alive again, put back in the queue
public class IPBalancer<T> {
    private final Queue<T> ipHolder;

    public IPBalancer(Collection<T> ips) {
        this.ipHolder = new ArrayBlockingQueue<>(ips.size());
        synchronized (ips) {
            for (T ip : ips) {
                ipHolder.add(ip);
            }
        }
    }

    public T getHost() {
        T host = ipHolder.poll();
        ipHolder.add(host);
        return host;
    }

}

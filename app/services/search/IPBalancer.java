package services.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

//TODO should be reviewed
public class IPBalancer<String> {
    private List<String> objectList = new ArrayList<String>();
    private Iterator<String> it;
    private final AtomicInteger position = new AtomicInteger(0);
    private volatile int positionInt = 0;
    private boolean isInit = false;
    private volatile int listSize;
    private int MAX_COUNTER;
    private String single;

    public IPBalancer() {
    }

    public synchronized void init(List<String> objects) {
        if (!isInit) {
            objectList.clear();
            objectList.addAll(objects);
            single = objects.get(0);
            it = objectList.iterator();
            listSize = objectList.size();
            MAX_COUNTER = objectList.size();
            //MAX_COUNTER = Integer.MAX_VALUE - 1 (Integer.MAX_VALUE - 1) % objectList.size();
            isInit = true;
        }
    }

    public void reInit() {
        isInit = false;
        init(objectList);
    }

    public Collection<String> getXAIURLList() {
        return objectList;
    }

    /**
     * Метод использует алгоритм round-robin для выбора адреса сервера.
     *
     * @return возвращает следующий сервер из списка серверов
     */
    public String getNext() {
        return objectList.get(getNextPosition() % listSize);
    }

    public String getNextOblListSize() {
        return objectList.get(getNextPosition());
    }

    public final int getNextPosition() {
        for (; ; ) {
            int current = position.get();
            int next = current + 1;
            if (next >= MAX_COUNTER) {
                next = 0;
            }
            if (position.compareAndSet(current, next))
                return current;
        }
    }


    public synchronized String getNext_Iterator() {
        if (!it.hasNext()) {
            it = objectList.iterator();
        }
        return it.next();
    }

    public synchronized String getNext_Long() {
        positionInt++;
        if (positionInt >= MAX_COUNTER) {
            positionInt = 0;
        }
        return objectList.get(positionInt);
    }

    public String getNextSingle() {
        return single;
    }
}

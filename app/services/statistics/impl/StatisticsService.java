package services.statistics.impl;


import play.Logger;
import services.statistics.DBService;

import java.util.ArrayList;
import java.util.List;

public abstract class StatisticsService<INPUT, OUTPUT> implements DBService<INPUT> {

    protected int BATCH_SIZE;
    protected boolean STATISTICS_ENABLE;

    private List<OUTPUT> list = new ArrayList<>(BATCH_SIZE);
    private final Object lock = new Object();

    @Override
    public void insert(INPUT entity) {
        if (!STATISTICS_ENABLE) {
            return;
        }
        OUTPUT record = generateOutput(entity);
        //TODO insert statistics type
        Logger.debug("Query '" + entity + "' is added to statistics ");

        List<OUTPUT> listToSave = null;

        synchronized (lock) {
            list.add(record);
            if (list.size() == BATCH_SIZE) {
                listToSave = list;
                list = new ArrayList<>(BATCH_SIZE);
            }
        }

        if (listToSave != null) {
            asyncSend(listToSave);

        }
    }

    protected abstract OUTPUT generateOutput(INPUT input);

    protected abstract void asyncSend(List<OUTPUT> listToSave);
}

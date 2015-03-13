package services.statistics.impl;

import datasources.dao.statistics.StatisticsDAO;
import datasources.dao.statistics.impl.DoubleQuerySqlDAOImpl;
import play.Logger;
import play.Play;
import play.libs.Akka;
import model.statistics.DoubleQueryRecord;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * stores DoubleQueryRecord entities in the List
 * and when the amount is greater or equals  BATCH_SIZE(can be changes in ok.statistics.groups.batchSize)
 * writes into Database
 * <p/>
 * Can be enabled/disabled: ok.statistics.groups.enable
 */
public class DoubleQueryStatisticsServiceImpl extends StatisticsService<DoubleQueryRecord, DoubleQueryRecord> {

    private static volatile DoubleQueryStatisticsServiceImpl instance;
    private StatisticsDAO<DoubleQueryRecord> operations;

    private DoubleQueryStatisticsServiceImpl() {
        this.operations = new DoubleQuerySqlDAOImpl();
        BATCH_SIZE = Play.application().configuration().getInt("ok.statistics.groups.batchSize", 15000);
        STATISTICS_ENABLE = Play.application().configuration().getBoolean("ok.statistics.groups.enable", Boolean.FALSE);
        Logger.debug("DoubleQueryStatisticsServiceImpl: statistic status: " + STATISTICS_ENABLE);
    }

    public static DoubleQueryStatisticsServiceImpl getInstance() {
        DoubleQueryStatisticsServiceImpl localInstance = instance;
        if (localInstance == null) {
            synchronized (DoubleQueryStatisticsServiceImpl.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DoubleQueryStatisticsServiceImpl();
                }
            }
        }
        return localInstance;
    }

    @Override
    protected DoubleQueryRecord generateOutput(DoubleQueryRecord record) {
        return record;
    }

    @Override
    protected void asyncSend(List<DoubleQueryRecord> listToSave) {
        final List<DoubleQueryRecord> finalListToSave = listToSave;
        Akka.future(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                operations.insertBatch(finalListToSave);
                return null;
            }
        });
    }

}

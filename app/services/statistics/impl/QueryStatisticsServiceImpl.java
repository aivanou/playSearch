package services.statistics.impl;

import akka.actor.ActorRef;
import akka.actor.Props;
import datasources.dao.statistics.impl.QuerySqlDAOImpl;
import play.Logger;
import play.Play;
import play.libs.Akka;
import model.statistics.StatisticsRecord;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * stores StatisticsRecord entities in the List
 * and when the amount is greater or equals  BATCH_SIZE(can be changes in ok.statistics.batchSize)
 * writes into Database
 * <p/>
 * Can be enabled/disabled: ok.statistics.enable
 * INPUT - String Query
 */
public class QueryStatisticsServiceImpl extends StatisticsService<String, StatisticsRecord> {

    private static volatile QueryStatisticsServiceImpl instance;

    private QueryStatisticsServiceImpl() {
        ActorRef myActor = Akka.system().actorOf(Props.create(MyActor.class));
        BATCH_SIZE = Play.application().configuration().getInt("ok.statistics.batchSize", 15000);
        STATISTICS_ENABLE = Play.application().configuration().getBoolean("ok.statistics.enable", Boolean.FALSE);
        Logger.debug("QueryStatisticsServiceImpl: statistic status: " + STATISTICS_ENABLE);
    }

    public static QueryStatisticsServiceImpl getInstance() {
        QueryStatisticsServiceImpl localInstance = instance;
        if (localInstance == null) {
            synchronized (QueryStatisticsServiceImpl.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new QueryStatisticsServiceImpl();
                }
            }
        }
        return localInstance;
    }

    @Override
    protected StatisticsRecord generateOutput(String query) {
        return new StatisticsRecord(query, System.currentTimeMillis());
    }

    @Override
    protected void asyncSend(List<StatisticsRecord> listToSave) {
        final List<StatisticsRecord> finalListToSave = listToSave;
        Akka.future(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                new QuerySqlDAOImpl().insertBatch(finalListToSave);
                return null;
            }
        });
    }
}

package services.actors;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import services.actors.messages.QueryStatistics;
import services.statistics.StatService;

import java.util.ArrayList;
import java.util.List;

/**
 * The actor who sends statistics to the outer service
 * Currently it is Apache Kafka
 * Accepts only @see QueryStatistics
 */
public class StatisticsActor extends UntypedActor {

    public static Props props(final StatService<QueryStatistics> statisticsSender) {
        return Props.apply(new Creator<StatisticsActor>() {

            @Override
            public StatisticsActor create() throws Exception {
                return new StatisticsActor(statisticsSender);
            }
        });
    }

    private StatService<QueryStatistics> sender;

    public StatisticsActor(StatService<QueryStatistics> sender) {
        this.sender = sender;
    }

    private final List<QueryStatistics> stats = new ArrayList<>();
    private final int MAX_BATCH = 200;
    private volatile int currentBatch = 0;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof ActorHandler.Initial) {
            System.out.println("Got initial message");
        } else if (message instanceof QueryStatistics) {
            System.out.println("received query message");
            currentBatch++;
            if (currentBatch >= MAX_BATCH) {
                sender.asyncSend(new ArrayList<>(stats));
                stats.clear();
                currentBatch = 0;
            }
        } else if (message instanceof ActorHandler.Shutdown) {
            sender.asyncSend(new ArrayList<>(stats));
            stats.clear();
        } else {
            unhandled(message);
        }
    }

    @Override
    public void postStop() {
        super.postStop();
    }
}

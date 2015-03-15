package services.actors;

import akka.actor.Props;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import services.actors.messages.QueryStatistics;
import services.statistics.StatService;

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

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof QueryStatistics) {
            //send
        } else if (message instanceof Terminated) {
        } else {
            unhandled(message);
        }
    }

    @Override
    public void postStop() {
        super.postStop();
    }
}

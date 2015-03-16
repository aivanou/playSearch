import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.Akka;
import services.actors.ActorHandler;
import services.actors.StatisticsActor;
import services.actors.messages.QueryStatistics;
import services.cache.impl.CacheFactory;
import services.statistics.impl.FakeStatisticsService;
import util.SearchConfiguration;

public class Global extends GlobalSettings {

    @Override
    public void onStart(Application app) {
        String file = "conf/" + app.configuration().getString("search.configuration") + ".json";
        try {
            SearchConfiguration.fromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            //TODO stop the app
        }
        CacheFactory.configure(app.configuration(), Akka.system().dispatcher().prepare());
        ActorHandler.configure("ActorSystem");
        ActorHandler.getInstance().registerActor(StatisticsActor.class, StatisticsActor.props(new FakeStatisticsService<QueryStatistics>()));
        ActorHandler.getInstance().sendMessage(StatisticsActor.class, new QueryStatistics(System.nanoTime(), "dummy query"));
        Logger.info("Application start...");
    }

    @Override
    public void onStop(Application app) {
        Logger.info("Application shutdown...");
    }


}

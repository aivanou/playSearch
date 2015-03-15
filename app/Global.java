import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.Akka;
import services.cache.impl.CacheFactory;

public class Global extends GlobalSettings {

    @Override
    public void onStart(Application app) {
//        if(Cache.isEnabled()) {
//            Akka.future(new Callable<Void>() {
//                @Override
//                public Void call() throws Exception {
//                    Couchbase.connect();
//                    return null;
//                }
//            });
//        }
        CacheFactory.configure(app.configuration(), Akka.system().dispatcher().prepare());
        Logger.info("Application start...");
    }

    @Override
    public void onStop(Application app) {
//        Couchbase.disconnect();

        Logger.info("Application shutdown...");
    }

}

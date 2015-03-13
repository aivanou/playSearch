package services.search.provider.impl.supervisor;

import model.SearchEngineType;

import java.util.concurrent.Callable;

/**
 * PingTask ...
 *
 * @author vadim
 * @date 1/9/13
 */
public abstract class PingTask implements Callable<Boolean> {
    public static final String PING_QUERY = "home";
    protected SearchEngineType engineType;

    public PingTask(SearchEngineType engineType) {
        this.engineType = engineType;
    }

    public String ofEngine() {
        return engineType.name();
    }
}

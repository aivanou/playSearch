package services.actors;

import akka.actor.*;
import scala.concurrent.duration.Duration;
import services.actors.messages.QueryStatistics;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;

/**
 * The initial point of actor system
 * invokes first level actors, defines standard messages
 * Handles the start, stop of actors
 */

public class ActorHandler {

    public static class Initial {
    }

    public static class Shutdown {
    }

    public static ActorHandler getInstance(String name) {
        return new ActorHandler(name);
    }

    private final Set<String> registeredActors = new ConcurrentSkipListSet<>();

    private final ActorSystem system;

    private final String USER_ACTOR_PATH;

    private ActorHandler(String name) {
        system = ActorSystem.create(name);
        USER_ACTOR_PATH = String.format("akka://%s/user", name);
    }

    public void registerActor(String path, Props props) {
        system.actorOf(props, path);
        registeredActors.add(path);
    }

    public <T extends UntypedActor> void registerActor(String path, Class<T> actorClass) {
        system.actorOf(Props.apply(actorClass), path);
        registeredActors.add(path);
    }

    public <T> void sendMessage(String path, T message) {
        if (!registeredActors.contains(path)) {
            //throw exception?
        }
        system.actorFor(String.format("%s/%s", USER_ACTOR_PATH, path)).tell(message);
    }

    public void sendMessage() {
        ActorRef a = system.actorFor("akka://SearchActorSystem/user/statistics");
        a.tell(QueryStatistics.fromSearchRequest("lalala"));
        akka.pattern.Patterns.gracefulStop(a, Duration.create(1, TimeUnit.SECONDS), system);
    }

    public void storActors() {
        system.actorFor("akka://SearchActorSystem/user/statistics").tell(PoisonPill.getInstance());
    }

    public static void main(String[] args) {
        System.out.println("hi");
    }

}

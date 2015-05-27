package services.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

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

    private static ActorHandler instance = null;

    public static void configure(String name) {
        if (instance == null)
            instance = new ActorHandler(name);
    }

    public static ActorHandler getInstance() {
        if (instance == null) {
            throw new AssertionError("actor system has no available instances, call configure first");
        }
        return instance;
    }

    private final Map<Class<? extends UntypedActor>, String> registeredActors = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final String name;

    private final ActorSystem system;

    private final String USER_ACTOR_PATH;

    private ActorHandler(String name) {
        this.name = name;
        system = ActorSystem.create(name);
        USER_ACTOR_PATH = String.format("akka://%s/user", name);
    }

    public <T extends UntypedActor> void registerActor(Class<T> clazz, Props props) {
        ActorRef actor = null;
        lock.lock();
        try {
            if (registeredActors.containsKey(clazz))
                return;
            String path = clazz.getName();
            System.out.println("Registering actor on path: " + path);
            actor = system.actorOf(props, path);
            registeredActors.put(clazz, path);
        } finally {
            lock.unlock();
        }
        if (actor != null) {
            actor.tell(new Initial());
        }
    }

    public <T extends UntypedActor> void registerActor(Class<T> actorClass) {
        registerActor(actorClass, Props.apply(actorClass));
    }

    public <T, K extends UntypedActor> void sendMessage(Class<K> clazz, T message) {
        lock.lock();
        try {
            if (!registeredActors.containsKey(clazz)) {
                //throw exception?
                return;
            }
        } finally {
            lock.unlock();
        }
        system.actorFor(String.format("%s/%s", USER_ACTOR_PATH, registeredActors.get(clazz))).tell(message);
    }

    public void storeActors() {
        lock.lock();
        try {
            for (String path : registeredActors.values()) {
                system.actorFor(String.format("%s/%s", USER_ACTOR_PATH, path)).tell(new Shutdown());
                //TODO: set a timeout for waiting before shutting down
                system.shutdown();
            }
        } finally {
            lock.unlock();
        }
    }
}

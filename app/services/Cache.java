package services;

import datasources.Couchbase;
import play.Logger;
import play.Play;

import java.io.*;

public class Cache {
    public static final int LIVE_PERIOD = 60 * 60 * 24 * 30; // 30 days
    private static Boolean enabled;

    public static boolean isActive() {
        return isEnabled() && Couchbase.isConnected();
    }

    public static boolean isEnabled() {
        if (enabled == null) {
            enabled = Play.application().configuration().getBoolean("cache.enabled");
        }
        return Boolean.TRUE == enabled;
    }

    public static Object get(String key) {
        Object value = Couchbase.getInstance().get(key);
        // deserialize object manualy because when use run play by "play run"
        // memcache classes don't see our classes, cause of special
        // classloader tree
        return value == null ? null : deserializeObject((byte[]) value);
    }

    public static void set(String key, Serializable value) {
        // serialize object manualy because when use run play by "play run"
        // memcache classes don't see our classes, cause of special
        // classloader tree
        byte[] serializedValue = serializeObject(value);
        Couchbase.getInstance().set(getKeyFromQuery(key), LIVE_PERIOD, serializedValue);
    }

    private static String getKeyFromQuery(String query) {
        return query.replaceAll(" ", "_").toLowerCase();
    }

    private static byte[] serializeObject(Serializable value) {

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(value);
            return bos.toByteArray();
        } catch (IOException e) {
            Logger.warn("Object can't be serialized", e);
        }
        return null;
    }

    private static Object deserializeObject(byte[] byteArray) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
             ObjectInput in = new ObjectInputStream(bis)) {
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Logger.warn("Object can't be deserialized", e);
        }
        return null;
    }
}

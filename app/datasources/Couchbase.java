package datasources;

import com.couchbase.client.CouchbaseClient;
import play.Configuration;
import play.Logger;
import play.Play;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates and manages connections to the Couchbase cluster
 * based on the given configuration settings.
 */
public final class Couchbase {
    /**
     * The instance of the client to connect to.
     */
    private static CouchbaseClient client = null;

    /**
     * Imported play configuration.
     */
    private static Configuration config = Play.application().configuration();

    /**
     * Make the constructor private so it will never be called directly.
     */
    private Couchbase() {
    }

    /**
     * Connect to a Couchbase cluster.
     */
    public synchronized static boolean connect() {
        String[] urls = config.getString("couchbase.urls").split(",");
        String bucket = config.getString("couchbase.bucket");
        String password = config.getString("couchbase.password");

        List<URI> hosts = new ArrayList<URI>();
        for (String cacheUrl : urls) {
            hosts.add(URI.create(cacheUrl));
        }

        try {
            client = new CouchbaseClient(hosts, bucket, password);
        } catch (Exception e) {
            Logger.error("Error creating Couchbase client: " + e.getMessage(), e);
        }
        Logger.debug("Connection to Couchbase is established");
        return true;
    }

    /**
     * Disconnect from a Couchbase cluster.
     */
    public synchronized static void disconnect() {
        if (client != null) {
            client.shutdown();
        }
    }

    public synchronized static boolean isConnected() {
        return client != null;
    }

    /**
     * Return the client object in a safe way. If the connection was
     * not opened previously, go ahead and create it.
     */
    public synchronized static CouchbaseClient getInstance() {
        if (client == null) {
            throw new IllegalStateException("Couchbase client is not initialized. Call Couchbase.connect() first.");
        }

        return client;
    }
}

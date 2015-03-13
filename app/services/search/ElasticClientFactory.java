package services.search;

import model.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import play.Logger;
import play.Play;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: aliaksandrhlinski
 * Date: 12/1/12
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ElasticClientFactory {

    private static Map<SearchType,TransportClient> clients;
    private static final String CLUSTER_NAME = Play.application().configuration().getString("elastic.name");

    public ElasticClientFactory() {
    }

    //TODO logging and processing exception should be improved
    public static TransportClient getInstance(SearchType searchType) throws Exception {
        TransportClient client = clients.get(searchType);
        if(client == null){
            Logger.error("No ElasticSearch client found for "+ searchType.toString());
            throw new Exception("No ElasticSearch client found for " + searchType.toString());
        }
        return client;
    }

    public static void init(Map<SearchType,List<String>> initParams){
        clients = new HashMap<SearchType,TransportClient>();

        for(SearchType contentType : SearchType.values()){
            Settings settings = ImmutableSettings.settingsBuilder()
                                        .put("cluster.name", CLUSTER_NAME).build();
            TransportClient client = new TransportClient(settings);

            for(final String address : initParams.get(contentType)) {
                String ip = address.split(":")[0];
                Integer port = Integer.parseInt(address.split(":")[1]);
                client.addTransportAddress(new InetSocketTransportAddress(ip,port));
            }

            clients.put(contentType, client);

        }
    }

}

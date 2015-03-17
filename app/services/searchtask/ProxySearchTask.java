package services.searchtask;

import asia.linky.killer.common.SearchEngine;
import asia.linky.killer.common.storage.HeaderStorage;
import asia.linky.killer.common.storage.ProxyStorage;
import com.ning.http.client.ProxyServer;
import play.Logger;
import play.libs.F;
import play.libs.WS;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;


/**
 * Was used with the requests to the google/bing through proxy servers
 */
@Deprecated
public class ProxySearchTask extends SearchTask {
    private static final int PROXY_PORT = 8888;
    private static final ProxyServer.Protocol PROXY_PROTOCOL = ProxyServer.Protocol.HTTP;
    private String query;
    private String requestURL;
    private SearchEngine searchEngine;
    private String proxyIP = ProxyStorage.getRandomIP();
    private HashMap<String, String> headers = HeaderStorage.getRandomHeaderSuite();

    public ProxySearchTask(String query, SearchEngine searchEngine, int timeout) {
        super(timeout);
        this.query = query;
        this.searchEngine = searchEngine;
        try {
            this.requestURL = String.format(searchEngine.getRequestUrlTemplate(), URLEncoder.encode(query, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Logger.error(e.getMessage());
        }
    }

    @Override
    public F.Promise<WS.Response> doSearch() {
        Logger.debug("Proxy IP: " + proxyIP + " Request URL: " + String.format(searchEngine.getRequestUrlTemplate(), query) + " User_Agent: " + headers.get("User-Agent"));
        ProxyServer proxy = new ProxyServer(PROXY_PROTOCOL, proxyIP, PROXY_PORT);
        return getRequest(requestURL, headers, proxy).execute();
    }
}

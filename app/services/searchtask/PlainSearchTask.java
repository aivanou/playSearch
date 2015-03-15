package services.searchtask;

import asia.linky.killer.common.SearchEngine;
import asia.linky.killer.common.storage.HeaderStorage;
import play.Logger;
import play.libs.F;
import play.libs.WS;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;


public class PlainSearchTask extends SearchTask {
    private String query;
    private String requestURL;
    private SearchEngine searchEngine;
    private HashMap<String, String> headers = HeaderStorage.getRandomHeaderSuite();

    public PlainSearchTask(String query, SearchEngine searchEngine, int timeout) {
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
        Logger.debug("Request URL: " + String.format(searchEngine.getRequestUrlTemplate(), query) + " User_Agent: " + headers.get("User-Agent"));
        return getRequest(requestURL, headers).execute();
    }
}

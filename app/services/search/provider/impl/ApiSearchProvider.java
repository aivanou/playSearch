package services.search.provider.impl;

import com.ning.http.client.Response;
import model.ResponseItem;
import model.SearchResponse;
import model.SearchType;
import org.codehaus.jackson.JsonNode;
import play.Logger;
import play.libs.WS;
import services.search.provider.SearchProvider;
import services.search.provider.impl.apiutil.URLBuilder;

import java.io.IOException;
import java.util.List;

/**
 * ApiSearchProvider ...
 *
 * @author Vadim Martos
 * @date 12/13/12
 */
public abstract class ApiSearchProvider implements SearchProvider {

    public static final int HTTP_UNAUTHORIZED = 403;
    public static final int HTTP_FORBIDDEN = 401;
    public static final int HTTP_SUCCESS = 200;

    protected final URLBuilder urlBuilder;

    protected ApiSearchProvider(URLBuilder urlBuilder) {
        this.urlBuilder = urlBuilder;
    }

    /**
     * Convert received from API JSON to SearchResponse
     *
     * @param response to be converted
     * @param type     is a type of returning response
     * @return response
     */
    protected SearchResponse convert(WS.Response response, SearchType type) {
        JsonNode json = response.asJson();
        Logger.trace(String.format("Got JSON: %s", json));
        List<ResponseItem> items = parse(json, type);
        Logger.trace(String.format("Got %s items from given JSON", items.size()));
        SearchResponse resp = new SearchResponse(type, items);
        Logger.debug(String.format("Got response: %s", resp));
        return resp;
    }

    protected SearchResponse convert(Response response, SearchType type) throws IOException {
        JsonNode json = play.libs.Json.parse(response.getResponseBody());
        Logger.trace(String.format("Got JSON: %s", json));
        List<ResponseItem> items = parse(json, type);
        Logger.trace(String.format("Got %s items from given JSON", items.size()));
        SearchResponse resp = new SearchResponse(type, items);
        Logger.debug(String.format("Got response: %s", resp));
        return resp;
    }

    /**
     * Parse JSON to concrete list of ResponseItem. If JSON is incorrect, empty list should be returned
     *
     * @param json to be parsed
     * @param type of returning items
     * @return result of parsing
     */
    protected abstract List<ResponseItem> parse(JsonNode json, SearchType type);
}

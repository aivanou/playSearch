package services.search.provider.impl;

import com.ning.http.client.Response;
import model.response.ContentResponse;
import model.response.ContentResponseMetadata;
import model.response.ResponseItem;
import model.response.SuccessContentResponse;
import org.codehaus.jackson.JsonNode;
import play.Logger;
import play.libs.WS;
import services.search.provider.SearchProvider;
import services.search.provider.impl.apiutil.URLBuilder;

import java.io.IOException;
import java.util.List;

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
    protected ContentResponse convert(WS.Response response, String type) {
        JsonNode json = response.asJson();
        Logger.trace(String.format("Got JSON: %s", json));
        List<ResponseItem> items = parse(json, type);
        Logger.trace(String.format("Got %s items from given JSON", items.size()));
        ContentResponse resp = new SuccessContentResponse(type, items, new ContentResponseMetadata(0, 0, ""));
        return resp;
    }

    protected ContentResponse convert(Response response, String type) throws IOException {
        JsonNode json = play.libs.Json.parse(response.getResponseBody());
        Logger.trace(String.format("Got JSON: %s", json));
        List<ResponseItem> items = parse(json, type);
        Logger.trace(String.format("Got %s items from given JSON", items.size()));
        ContentResponse resp = new SuccessContentResponse(type, items, new ContentResponseMetadata(0, 0, ""));
        return resp;
    }

    /**
     * Parse JSON to concrete list of ResponseItem. If JSON is incorrect, empty list should be returned
     *
     * @param json to be parsed
     * @param type of returning items
     * @return result of parsing
     */
    protected abstract List<ResponseItem> parse(JsonNode json, String type);
}

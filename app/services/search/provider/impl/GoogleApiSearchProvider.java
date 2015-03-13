package services.search.provider.impl;

import com.akavita.metasearch.keystorage.domain.GoogleKey;
import com.akavita.metasearch.keystorage.service.KeyProvider;
import model.ResponseItem;
import model.SearchRequest;
import model.SearchResponse;
import model.SearchType;
import org.codehaus.jackson.JsonNode;
import play.Logger;
import play.libs.F;
import play.libs.WS;
import services.search.provider.SearchProvider;
import services.search.provider.impl.apiutil.GoogleURLBuilder;
import services.search.provider.impl.apiutil.URLBuilder;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * GoogleApiSearchProvider ...
 *
 * @author Vadim Martos
 * @date 12/11/12
 */
public final class GoogleApiSearchProvider extends ApiSearchProvider implements SearchProvider {
    private static final String ITEMS = "items";
    private static final String LINK = "link";
    private static final String TITLE = "title";
    private static final String SNIPPET = "snippet";

    private final KeyProvider<GoogleKey> provider;

    public GoogleApiSearchProvider(URLBuilder urlBuilder, KeyProvider<GoogleKey> provider) {
        super(urlBuilder);
        this.provider = provider;
    }

    public GoogleApiSearchProvider(KeyProvider<GoogleKey> provider) {
        this(GoogleURLBuilder.defaultInstance(), provider);
    }

    @Override
    public F.Promise<SearchResponse> doSearch(final SearchType type, final SearchRequest req) throws IllegalArgumentException {
        Logger.debug(String.format("Google API accepted: %s", req));
        if (type != SearchType.DOCS) {
            throw new IllegalArgumentException(String.format("Google API can't be used for searching through '%s'; documents only", type));
        }
        String url = urlBuilder.build(req);
        Logger.debug(String.format("Google API: build url %s", url));
        WS.WSRequestHolder holder = WS.url(url).setTimeout(TIMEOUT);
        Map<String, String> qParams = urlBuilder.getQueryParams(url);
        for (Map.Entry<String, String> q : qParams.entrySet()) {
            holder.setQueryParameter(q.getKey(), q.getValue());
        }
        final GoogleKey key = provider.getValidKey();
        if (key == null) {
            Logger.warn("Has no key for Google API");
            return F.Promise.pure(new SearchResponse(type));
        }
        Logger.debug("Using key cx: " + key.getValue().getCx());
        Logger.debug("Using key : " + key.getValue().getKey());
        holder.setQueryParameter(GoogleURLBuilder.CX, key.getValue().getCx());
        holder.setQueryParameter(GoogleURLBuilder.KEY, key.getValue().getKey());
        return holder.get().map(new F.Function<WS.Response, SearchResponse>() {
            @Override
            public SearchResponse apply(WS.Response response) throws Throwable {
                int code = response.getStatus();
                Logger.debug("Code " + code);
                switch (code) {
                    case HTTP_UNAUTHORIZED: {
                        Logger.warn(String.format("Key %s is not valid for Google API, code %s", key.getValue().getKey(), code));
                        return new SearchResponse(type);
                    }
                    case HTTP_SUCCESS: {
                        return convert(response, type);
                    }
                    default: {
                        Logger.warn(String.format("Key %s is not valid for Google API, code %s", key.getValue().getKey(), code));
                        return new SearchResponse(type);
                    }
                }
            }
        }).recover(new F.Function<Throwable, SearchResponse>() {
            @Override
            public SearchResponse apply(Throwable throwable) throws Throwable {
                Logger.error(String.format("Failed to access key API for Bing because of: %s", throwable.getLocalizedMessage()));
                return new SearchResponse(type);
            }
        });
    }

    @Override
    protected List<ResponseItem> parse(JsonNode json, SearchType type) {
        long ms = System.currentTimeMillis();
        Logger.trace(String.format("Parsing JSON: %s", json));
        List<ResponseItem> items = new LinkedList<ResponseItem>();
        if (json.has(ITEMS)) {
            double score = 1d;
            JsonNode array = json.get(ITEMS);
            for (Iterator<JsonNode> i = array.getElements(); i.hasNext(); ) {
                JsonNode next = i.next();
                if (!next.has(TITLE)) {
                    Logger.debug(String.format("JSON has no element with name %s, skipping; json: %s", TITLE, next));
                } else if (!next.has(SNIPPET)) {
                    Logger.debug(String.format("JSON has no element with name %s, skipping; json: %s", SNIPPET, next));
                } else if (!next.has(LINK)) {
                    Logger.debug(String.format("JSON has no element with name %s, skipping; json: %s", LINK, next));
                } else {
                    String title = next.get(TITLE).asText();
                    String snippet = next.get(SNIPPET).asText();
                    String url = next.get(LINK).asText();
                    ResponseItem item = new ResponseItem(url, title, snippet, type, score);
                    items.add(item);
                    Logger.trace(String.format("Another item parsed: %s", item));
                    score /= 2d;
                }
            }
        } else {
            Logger.debug(String.format("JSON has no element with name %s; json: %s", ITEMS, json));
        }
        Logger.trace(String.format("Finished parsing of %s items in %s msec", items.size(), System.currentTimeMillis() - ms));
        return items;
    }
}

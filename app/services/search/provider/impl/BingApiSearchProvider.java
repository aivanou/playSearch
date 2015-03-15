package services.search.provider.impl;

import com.akavita.metasearch.keystorage.domain.StringKey;
import com.akavita.metasearch.keystorage.service.KeyProvider;
import model.response.ResponseItem;
import model.SearchType;
import model.request.ContentRequest;
import model.response.ContentResponse;
import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonNode;
import play.Logger;
import play.libs.F;
import play.libs.WS;
import services.search.provider.impl.apiutil.BingURLBuilder;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * BingApiSearchProvider ...
 *
 * @author Vadim Martos
 * @date 12/11/12
 */
public final class BingApiSearchProvider extends ApiSearchProvider {
    private static final String D = "d";
    private static final String RESULTS = "results";
    private static final String TITLE = "Title";
    private static final String DESCRIPTION = "Description";
    private static final String URL = "Url";

    private final KeyProvider<StringKey> provider;

    public BingApiSearchProvider(KeyProvider<StringKey> provider) {
        super(BingURLBuilder.defaultInstance());
        this.provider = provider;
    }

    @Override
    public F.Promise<ContentResponse> doSearch(final ContentRequest req) throws IllegalArgumentException {
        Logger.debug(String.format("Bing API accepted: %s", req));
        if (req.getSearchType() != SearchType.DOCS) {
            throw new IllegalArgumentException(String.format("Bing API can't be used for searching through '%s'; documents only", req.getSearchType()));
        }
        StringKey sKey = provider.getValidKey();
        if (sKey == null) {
            Logger.warn("Has no key for Bing API");
//            return F.Promise.pure(new SearchResponse());
            return null;
        }
        final String key = sKey.getValue().getKey();
        final String encrKey = String.format("Basic %s", correct(key));
//        final String url = urlBuilder.build(req);
        final String url = null;
        Logger.debug(String.format("Bing API goes to url: %s", url));


        WS.WSRequestHolder holder = WS.url(url).setTimeout(TIMEOUT).setHeader("Authorization", encrKey);
        Map<String, String> qParams = urlBuilder.getQueryParams(url);
        for (Map.Entry<String, String> q : qParams.entrySet()) {
            holder.setQueryParameter(q.getKey(), q.getValue());
        }
//        return holder.get().map(new F.Function<WS.Response, SearchResponse>() {
//            @Override
//            public SearchResponse apply(WS.Response response) throws Throwable {
//                int code = response.getStatus();
//                Logger.debug("Code " + code);
//                switch (code) {
//                    case HTTP_UNAUTHORIZED: {
//                        Logger.warn(String.format("Key %s is not valid for Bing API, code %s", key, code));
////                        return new SearchResponse();
//                        return null;
//                    }
//                    case HTTP_SUCCESS: {
//                        return convert(response, type);
//                    }
//                    default: {
//                        Logger.warn(String.format("Key %s is not valid for Bing API, code %s", key, code));
////                        return new SearchResponse();
//                        return null;
//                    }
//                }
//            }
//        }).recover(new F.Function<Throwable, SearchResponse>() {
//            @Override
//            public SearchResponse apply(Throwable throwable) throws Throwable {
//                Logger.error(String.format("Failed to access key API for Bing because of: %s", throwable.getLocalizedMessage()));
////                return new SearchResponse(type);
//                return null;
//            }
//        });

        return null;
    }

    /**
     * Encoding key using Base64 algorithm
     *
     * @param key to encode
     * @return Base64-encoded key
     */
    private String correct(String key) {
        String encoded = new String(Base64.encodeBase64((String.format("%s:%s", key, key)).getBytes())).intern();
        Logger.debug(String.format("Encoded key [%s] to [%s] using Base64", key, encoded));
        return encoded;
    }

    @Override
    protected List<ResponseItem> parse(JsonNode json, SearchType type) {
        long ms = System.currentTimeMillis();
        Logger.trace(String.format("Parsing JSON: %s", json));
        List<ResponseItem> items = new LinkedList<ResponseItem>();
        if (json.has(D)) {
            JsonNode array = json.get(D);
            if (array.has(RESULTS)) {
                double score = 1d;
                for (Iterator<JsonNode> i = array.get(RESULTS).getElements(); i.hasNext(); ) {
                    JsonNode next = i.next();
                    if (!next.has(TITLE)) {
                        Logger.debug(String.format("JSON has no element with name %s, skipping; json: %s", TITLE, next));
                    } else if (!next.has(DESCRIPTION)) {
                        Logger.debug(String.format("JSON has no element with name %s, skipping; json: %s", DESCRIPTION, next));
                    } else if (!next.has(URL)) {
                        Logger.debug(String.format("JSON has no element with name %s, skipping; json: %s", URL, next));
                    } else {
                        String title = next.get(TITLE).asText();
                        String snippet = next.get(DESCRIPTION).asText();
                        String url = next.get(URL).asText();
                        ResponseItem item = new ResponseItem(url, title, snippet, type, score, System.nanoTime());
                        items.add(item);
                        Logger.trace(String.format("Another item parsed: %s", item));
                        score /= 2d;
                    }
                }
            } else {
                Logger.debug(String.format("JSON has no element with name %s; json: %s", RESULTS, array));
            }
        } else {
            Logger.debug(String.format("JSON has no element with name %s; json: %s", D, json));
        }
        Logger.trace(String.format("Finished parsing of %s items in %s msec", items.size(), System.currentTimeMillis() - ms));
        return items;
    }
}

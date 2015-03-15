package services.search.provider.impl.apiutil;

import model.request.SearchRequest;
import play.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * URLBuilder ...
 *
 * @author vadim
 * @date 12/12/12
 */
public abstract class URLBuilder {
    protected final String base;

    protected URLBuilder(String base) {
        this.base = base;
    }

    /**
     * Build API-depended url from request, that will be used in ApiSearchProvider in future.
     *
     * @param req to build url from
     * @return url for API provider
     * @throws IllegalArgumentException if request is invalid; {@see validate(SearchRequest request)} method
     */
    public abstract String build(SearchRequest req) throws IllegalArgumentException;

    /**
     * Validate request for accuracy. Request should has not nullable and not empty query,
     * language and region, to complete validation. Otherwise, IllegalArgumentException will be thrown
     *
     * @param request to check
     * @throws IllegalArgumentException if validation fails
     */
    protected void validate(SearchRequest request) throws IllegalArgumentException {
        if (request.getQuery() == null || request.getQuery().isEmpty()) {
            throw new IllegalArgumentException(String.format("Query of request can't be null or empty: %s", request));
        } else if (request.getLang() == null || request.getLang().isEmpty()) {
            throw new IllegalArgumentException(String.format("Lang of request can't be null or empty: %s", request));
        } else if (request.getRegion() == null || request.getRegion().isEmpty()) {
            throw new IllegalArgumentException(String.format("Region of request can't be null or empty: %s", request));
        }
    }

    /**
     * Parsing url and return all query parameters as map. Parameter must delimited with '&' character.
     * Key and value inside parameter must be delimited with '=' character. Invalid url will make method to return empty
     * map. Invalid params will be skipped.
     *
     * @param url to parse
     * @return query params as key-value pairs in map
     */
    public Map<String, String> getQueryParams(String url) {
        Map<String, String> params = new LinkedHashMap<String, String>();
        String query = null;
        try {
            query = new URL(url).getQuery();
        } catch (MalformedURLException ignore) {
            Logger.debug("Bad url for calculating query params: " + url);
        }
        if (query != null) {
            String[] parts = query.split("&");
            if (parts != null && parts.length > 0) {
                for (String s : parts) {
                    String[] value = s.split("=");
                    if (value.length == 2) {
                        params.put(value[0], value[1]);
                    }
                }
            }
        }
        return params;
    }
}

package model.request;

import model.SearchEngineType;
import model.SearchType;
import model.input.ContentDTO;
import model.input.InputDTO;
import services.search.provider.SearchProvider;
import services.search.provider.impl.ElasticSearchProvider;
import util.SearchConfiguration;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The Search request, builds from @see InputDTO
 * Contains the set of content requests for every search provider
 *
 * @sese SearchProvider for available providers
 * <p/>
 * Currently supports the internal providers
 * @see model.request.ContentRequest for more information about content requests
 * @see services.search.provider.impl.ElasticSearchProvider
 * <p/>
 * The External providers were turned off due to lack of resources to run them(API key and proxies)
 * @see model.input.InputDTO uses it to build the request
 */
public class SearchRequest {

    public static SearchRequest fromDTO(InputDTO dto) throws ValidationException {
        Set<ContentRequest> requests = new HashSet<>();
        Map<Class<? extends SearchProvider>, Set<ContentRequest>> contentRequests = new HashMap<>();
        Set<SearchEngineType> engines = new HashSet<>();
        for (ContentDTO cdto : dto.getContents()) {
            SearchType stype = SearchConfiguration.getInstance().getSearchTypeByName(cdto.getType());
            if (stype == null) {
                throw new ValidationException(String.format("Type %s is not supported", cdto.getType()));
            }
            requests.add(InternalContentRequest.from(cdto, dto.getQuery()));
        }
        contentRequests.put(ElasticSearchProvider.class, requests);
        //TODO if necessary put the external content requests here
        for (String engine : dto.getSearchEngines()) {
            SearchEngineType engineType = SearchConfiguration.getInstance().getEngineByName(engine);
            if (engineType == null) {
                throw new ValidationException(String.format("Engine %s is not supported", engine));
            }
            engines.add(engineType);
        }
        return new SearchRequest(dto.getQuery(), dto.getLang(), dto.getRegion(), dto.getTotalNumber(), engines, contentRequests);
    }

    private String query;
    private String lang;
    private String region;
    private int totalNumber;
    private Set<SearchEngineType> searchEngines;
    private Map<Class<? extends SearchProvider>, Set<ContentRequest>> contentRequests;

    public SearchRequest(String query, String lang, String region, int totalNumber) {
        this.query = query;
        this.lang = lang;
        this.region = region;
        this.totalNumber = totalNumber;
        this.searchEngines = new HashSet<>();
        this.contentRequests = new HashMap<>();
    }

    public SearchRequest(String query, String lang, String region,
                         int totalNumber, Set<SearchEngineType> searchEngines,
                         Map<Class<? extends SearchProvider>, Set<ContentRequest>> contentRequests) {
        this.query = query;
        this.lang = lang;
        this.region = region;
        this.totalNumber = totalNumber;
        this.searchEngines = searchEngines;
        this.contentRequests = contentRequests;
    }

    public String getQuery() {
        return query;
    }

    public String getLang() {
        return lang;
    }

    public String getRegion() {
        return region;
    }

    public void addSearchEngine(SearchEngineType provider) {
        searchEngines.add(provider);
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public Set<SearchEngineType> getSearchEngines() {
        return searchEngines;
    }

    public Set<ContentRequest> getRequests(Class<? extends SearchProvider> provider) {
        if (contentRequests.containsKey(provider))
            return contentRequests.get(provider);
        return null;
    }

    //TODO add toString method
}
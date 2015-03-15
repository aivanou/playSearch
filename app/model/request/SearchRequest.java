package model.request;

import model.SearchEngineType;
import model.SearchType;
import model.input.ContentDTO;
import model.input.InputDTO;
import services.util.ToString;

import javax.validation.ValidationException;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * The Search request, builds from @see InputDTO
 */
public class SearchRequest {

    public static SearchRequest fromDTO(InputDTO dto) throws ValidationException {
        Set<ContentRequest> requests = new HashSet<>();
        Set<SearchEngineType> engines = new HashSet<>();
        for (ContentDTO cdto : dto.getContents()) {
            SearchType stype = SearchType.getByName(cdto.getType());
            if (stype.equals(SearchType.UNDEFINED)) {
                throw new ValidationException(String.format("Type %s is not supported", cdto.getType()));
            }
            requests.add(new ContentRequest(dto.getQuery(), SearchType.getByName(cdto.getType()), cdto.getNumber(), cdto.getFrom()));
        }
        for (String engine : dto.getSearchEngines()) {
            SearchEngineType engineType = SearchEngineType.getById(engine);
            if (engineType.equals(SearchEngineType.Unrecognised)) {
                throw new ValidationException(String.format("Engine %s is not supported", engine));
            }
            engines.add(engineType);
        }
        return new SearchRequest(dto.getQuery(), dto.getLang(), dto.getRegion(), dto.getTotalNumber(), engines, requests);
    }

    private String query;
    private String lang;
    private String region;
    private int totalNumber;
    private Set<SearchEngineType> searchEngines;
    private Set<ContentRequest> contentRequests;

    public SearchRequest(String query, String lang, String region, int totalNumber) {
        this.query = query;
        this.lang = lang;
        this.region = region;
        this.totalNumber = totalNumber;
        this.searchEngines = EnumSet.noneOf(SearchEngineType.class);
        this.contentRequests = new HashSet<>();
    }

    public SearchRequest(String query, String lang, String region, int totalNumber, Set<SearchEngineType> searchEngines, Set<ContentRequest> contentRequests) {
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

    public Set<ContentRequest> getContentRequests() {
        return contentRequests;
    }

    public void addContentRequest(ContentRequest contentRequest) {
        contentRequests.add(contentRequest);
    }

    public String toString() {
        return new ToString(this).add("query", query).add("lang", lang)
                .add("region", region).addGroup("engines", searchEngines).
                        addGroup("types", contentRequests).toString();
    }
}
package model;

import model.input.InputDTO;
import services.util.ToString;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class SearchRequest {

    public static SearchRequest fromDTO(InputDTO dto) {
        return new SearchRequest(dto.getQuery(), dto.getLang(), dto.getRegion(), dto.getPage(), dto.getTotalNumber());
    }

    private String query;
    private String lang;
    private String region;
    private int page;
    private int totalNumber;
    private Set<SearchEngineType> searchEngines;
    private Set<ContentRequest> contentRequests;

    private SearchRequest(String query, String lang, String region, int page, int totalNumber) {
        this.query = query;
        this.lang = lang;
        this.region = region;
        this.page = page;
        this.totalNumber = totalNumber;
        this.searchEngines = EnumSet.noneOf(SearchEngineType.class);
        this.contentRequests = new HashSet<>();
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void addSearchEngine(SearchEngineType provider) {
        searchEngines.add(provider);
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
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
        return new ToString(this).add("query", query).add("page", page).add("lang", lang)
                .add("region", region).addGroup("engines", searchEngines).
                        addGroup("types", contentRequests).toString();
    }
}
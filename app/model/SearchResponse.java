package model;

import services.util.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class SearchResponse {

    private SearchType searchType;
    private List<ResponseItem> items = new ArrayList<>();
    private SearchMetadata metadata;

    public SearchResponse(SearchType searchType) {
        this.searchType = searchType;
    }

    public SearchResponse() {
        this.metadata = new SearchMetadata();
    }

    public SearchResponse(SearchType searchType, List<ResponseItem> items) {
        this.searchType = searchType;
        this.items.addAll(items);
    }

    public SearchResponse(SearchType searchType, SearchMetadata metadata) {
        this.searchType = searchType;
        this.metadata = metadata;
    }

    public SearchMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(SearchMetadata metadata) {
        this.metadata = metadata;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    public List<ResponseItem> getItems() {
        return items;
    }

    public void addItems(Collection<ResponseItem> items) {
        this.items.addAll(items);
    }

    public void addItem(ResponseItem item) {
        items.add(item);
    }

    @Override
    public String toString() {
        return new ToString(this).add("type", searchType).addGroup("items", items).toString();
    }
}

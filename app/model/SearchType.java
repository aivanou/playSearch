package model;

import java.util.List;
import java.util.Map;

/**
 * The request contains several search types, that are specified in the query.json config file
 * Every type has its own set of hosts and search fields
 * <p/>
 * For example: the web documets index can be situated at several hosts and
 * can be consisted of several searchable fields
 */
public class SearchType {

    private String name;
    private List<String> searchFields;
    private Map<SearchEngineType, List<String>> hosts;


    public SearchType(String name, List<String> searchFields, Map<SearchEngineType, List<String>> hosts) {
        this.name = name;
        this.searchFields = searchFields;
        this.hosts = hosts;
    }

    public String getName() {
        return name;
    }

    public List<String> getSearchFields() {
        return searchFields;
    }

    public Map<SearchEngineType, List<String>> getHosts() {
        return hosts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchType that = (SearchType) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "SearchType{" +
                "name='" + name + '\'' +
                ", searchFields=" + searchFields +
                ", hosts=" + hosts +
                '}';
    }
}


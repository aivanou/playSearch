package model;

import java.util.List;

public class SearchType {

    private String name;
    private List<String> searchFields;

    public SearchType(String name, List<String> searchFields) {
        this.name = name;
        this.searchFields = searchFields;
    }

    public String getName() {
        return name;
    }

    public List<String> getSearchFields() {
        return searchFields;
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
                '}';
    }
}


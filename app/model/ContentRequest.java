package model;

import model.input.ContentDTO;

public class ContentRequest {

    private SearchType searchType;

    private int number;

    public static ContentRequest from(ContentDTO dto) {
        return new ContentRequest(SearchType.getByName(dto.getType()), dto.getNumber());
    }

    public ContentRequest(SearchType name, int number) {
        this.searchType = name;
        this.number = number;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContentRequest that = (ContentRequest) o;

        if (number != that.number) return false;
        if (searchType != null ? !searchType.equals(that.searchType) : that.searchType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = searchType != null ? searchType.hashCode() : 0;
        result = 31 * result + number;
        return result;
    }

    @Override
    public String toString() {
        return "ContentRequest{" +
                "searchType='" + searchType + '\'' +
                ", number=" + number +
                '}';
    }
}

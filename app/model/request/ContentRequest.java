package model.request;

import model.SearchType;
import model.input.ContentDTO;

/**
 * The typed request @see SearchType for available types
 */
public class ContentRequest {

    private SearchType searchType;

    String query;
    private int number;
    private int from;

    public static ContentRequest from(ContentDTO dto, String query) {
        return new ContentRequest(query, SearchType.getByName(dto.getType()), dto.getNumber(), dto.getFrom());
    }

    public ContentRequest(String query, SearchType stype, int number, int from) {
        this.query = query;
        this.searchType = stype;
        this.from = from;
        this.number = number;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public int getNumber() {
        return number;
    }

    public int getFrom() {
        return from;
    }

    public String getQuery() {
        return query;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContentRequest that = (ContentRequest) o;

        if (from != that.from) return false;
        if (number != that.number) return false;
        if (query != null ? !query.equals(that.query) : that.query != null) return false;
        if (searchType != that.searchType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = searchType != null ? searchType.hashCode() : 0;
        result = 31 * result + (query != null ? query.hashCode() : 0);
        result = 31 * result + number;
        result = 31 * result + from;
        return result;
    }

    @Override
    public String toString() {
        return "ContentRequest{" +
                "searchType=" + searchType +
                ", query='" + query + '\'' +
                ", number=" + number +
                ", from=" + from +
                '}';
    }
}

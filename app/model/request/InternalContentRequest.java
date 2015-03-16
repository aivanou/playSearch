package model.request;


import model.SearchType;
import model.input.ContentDTO;
import util.SearchConfiguration;

public class InternalContentRequest extends ContentRequest {

    private SearchType searchType;

    public static ContentRequest from(ContentDTO dto, String query) {
        return new InternalContentRequest(query, SearchConfiguration.getInstance().getSearchTypeByName(dto.getType()), dto.getNumber(), dto.getFrom());
    }

    public InternalContentRequest(String query, SearchType stype, int number, int from) {
        super(query, number, from);
        this.searchType = stype;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        InternalContentRequest that = (InternalContentRequest) o;

        if (searchType != null ? !searchType.equals(that.searchType) : that.searchType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (searchType != null ? searchType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "InternalContentRequest{" +
                "searchType=" + searchType +
                ", query='" + query + '\'' +
                ", number=" + number +
                ", from=" + from +
                '}';
    }

}

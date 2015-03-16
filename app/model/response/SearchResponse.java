package model.response;

import model.Jsonable;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The search response, aggregates a set of @see model.response.ContentResponse and @see model.response.SearchResponseMetadata
 */

public class SearchResponse implements Jsonable {

    private List<ContentResponse> items;
    private SearchResponseMetadata metadata;

    public SearchResponse(List<ContentResponse> items, SearchResponseMetadata metadata) {
        this.items = items;
        this.metadata = metadata;
    }

    public SearchResponse(SearchResponseMetadata metadata) {
        this.metadata = metadata;
        this.items = new ArrayList<>();
    }

    public List<ContentResponse> getItems() {
        return items;
    }

    public SearchResponseMetadata getMetadata() {
        return metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchResponse that = (SearchResponse) o;

        if (items != null ? !items.equals(that.items) : that.items != null) return false;
        if (metadata != null ? !metadata.equals(that.metadata) : that.metadata != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = items != null ? items.hashCode() : 0;
        result = 31 * result + (metadata != null ? metadata.hashCode() : 0);
        return result;
    }

    @Override
    public String toJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
//        JsonNode n = new
        return mapper.writeValueAsString(this);
    }
}

package model.response;


import model.SearchType;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class SuccessContentResponse extends ContentResponse {

    private List<ResponseItem> items;
    private ContentResponseMetadata metadata;

    public static SuccessContentResponse buildFromElasticJson(JsonNode node, String query) {
        ContentResponseMetadata metadata = ContentResponseMetadata.fromElasticJson(node, query);
        System.out.println(metadata);
        JsonNode hits = node.get("hits").get("hits");
        Iterator<JsonNode> it = hits.getElements();
        List<ResponseItem> items = new ArrayList<>();
        while (it.hasNext()) {
            JsonNode n = it.next();
            System.out.println(n);
            ResponseItem item = ResponseItem.buildFromJson(n);
            items.add(item);
        }
        return new SuccessContentResponse(SearchType.DOCS, items, metadata);
    }

    public SuccessContentResponse(SearchType searchType, List<ResponseItem> items, ContentResponseMetadata metadata) {
        super(searchType);
        this.items = new ArrayList<>(items);
        this.metadata = metadata;
    }

    public SuccessContentResponse(ContentResponseMetadata metadata, SearchType searchType) {
        super(searchType);
        this.metadata = metadata;
        this.items = new ArrayList<>();
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public List<ResponseItem> getItems() {
        return items;
    }

    public ContentResponseMetadata getMetadata() {
        return metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SuccessContentResponse that = (SuccessContentResponse) o;

        if (items != null ? !items.equals(that.items) : that.items != null) return false;
        if (metadata != null ? !metadata.equals(that.metadata) : that.metadata != null) return false;
        if (searchType != that.searchType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = searchType != null ? searchType.hashCode() : 0;
        result = 31 * result + (items != null ? items.hashCode() : 0);
        result = 31 * result + (metadata != null ? metadata.hashCode() : 0);
        return result;
    }

    @Override
    public String toJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}

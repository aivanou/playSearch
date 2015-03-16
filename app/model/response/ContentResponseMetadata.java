package model.response;

import model.Jsonable;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;

/**
 * Saves the information about seach provider work
 * Used by @see model.response.SuccessContentResponse
 */
public class ContentResponseMetadata implements Serializable, Jsonable {

    private static final long serialVersionUID = 899171295662716147L;

    private int nresults;
    private int queryTime;
    private String query;

    public static ContentResponseMetadata fromElasticJson(JsonNode response, String query) {
        int time = Integer.parseInt(response.get("took").asText());
        int total = Integer.parseInt(response.get("hits").get("total").asText());
        return new ContentResponseMetadata(total, time, query);
    }

    public ContentResponseMetadata(int nresults, int queryTime, String query) {
        this.nresults = nresults;
        this.queryTime = queryTime;
        this.query = query;
    }

    public int getNresults() {
        return nresults;
    }

    public int getQueryTime() {
        return queryTime;
    }

    public String getQuery() {
        return query;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContentResponseMetadata that = (ContentResponseMetadata) o;

        if (nresults != that.nresults) return false;
        if (queryTime != that.queryTime) return false;
        if (query != null ? !query.equals(that.query) : that.query != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = nresults;
        result = 31 * result + queryTime;
        result = 31 * result + (query != null ? query.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ContentResponseMetadata{" +
                "nresults=" + nresults +
                ", queryTime=" + queryTime +
                ", query='" + query + '\'' +
                '}';
    }

    @Override
    public String toJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}

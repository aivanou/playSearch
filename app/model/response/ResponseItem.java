package model.response;

import model.Jsonable;
import model.Schema;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import services.util.ToString;

import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Every @see model.response.SuccessContentResponse aggregates a set of ResponseItems as a result entities
 */
public class ResponseItem implements Comparable<ResponseItem>, Comparator<ResponseItem>, Serializable, Jsonable {

    private static final long serialVersionUID = 7546471295662716147L;
    private static final String FAVICON_SERVICE = "http://g.etfv.co/";
    protected String url;
    protected String title;
    protected String snippet;
    protected String type;
    protected double score;
    protected long indexed;
    protected Map<String, String> params = new HashMap<String, String>();

    public static ResponseItem buildFromJson(JsonNode node) {
        String type = node.get("_type").asText().toUpperCase();
        double score = node.get("_score").asDouble();
        Iterator<Map.Entry<String, JsonNode>> fieldIt = node.get("_source").getFields();
        String content = "";
        String title = "";
        String url = "";
        long indexed = 0;
        Map<String, String> params = new HashMap<>();

        while (fieldIt.hasNext()) {
            Map.Entry<String, JsonNode> entry = fieldIt.next();
            if (entry.getKey().equals(Schema.CONTENT)) {
                content = entry.getValue().asText();
            } else if (entry.getKey().equals(Schema.TITLE)) {
                title = entry.getValue().asText();
            } else if (entry.getKey().equals(Schema.URL)) {
                url = entry.getValue().asText();
            } else if (entry.getKey().equals(Schema.INDEXED_TIME)) {
                indexed = entry.getValue().asLong();
            } else {
                params.put(entry.getKey(), entry.getValue().asText());
            }
        }
        return new ResponseItem(url, title, content, type, score, indexed);
    }

    public ResponseItem(String url, String title, String snippet, String type, double score, long indexed) {
        this.url = url;
        this.title = title;
        this.snippet = snippet;
        this.type = type;
        this.score = score;
        this.indexed = indexed;
    }

    private String favicon(String url) {
        return new StringBuilder(FAVICON_SERVICE.length() + url.length()).append(FAVICON_SERVICE).append(url).toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResponseItem that = (ResponseItem) o;

        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static String getFaviconService() {
        return FAVICON_SERVICE;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getSnippet() {
        return snippet;
    }

    public double getScore() {
        return score;
    }

    public String getType() {
        return type;
    }

    public long getIndexed() {
        return indexed;
    }

    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public int compareTo(ResponseItem t) {
        return Double.compare(t.score, this.score);
    }

    @Override
    public int compare(ResponseItem t, ResponseItem t1) {
        return Double.compare(t1.getScore(), t.getScore());
    }

    @Override
    public String toString() {
        return new ToString(this).add("score", score).add("url", url).add("type", type).toString();
    }

    @Override
    public String toJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}

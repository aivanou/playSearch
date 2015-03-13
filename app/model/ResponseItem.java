package model;

import services.util.ToString;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ResponseItem implements Comparable<ResponseItem>, Comparator<ResponseItem>, Serializable {

    private static final long serialVersionUID = 7546471295662716147L;
    private static final String FAVICON_SERVICE = "http://g.etfv.co/";
    protected String url;
    protected String title;
    protected String snippet;
    protected double score;
    protected SearchType type;
    protected long indexed;
    protected Map<String,String> params = new HashMap<String,String>();

    public ResponseItem() {
    }

    public ResponseItem(String url, String title, String snippet, SearchType type, double score) {
        this.url = url;
        this.title = title;
        this.snippet = snippet;
        this.type = type;
        this.score = score;
    }

    private String favicon(String url) {
        return new StringBuilder(FAVICON_SERVICE.length() + url.length()).append(FAVICON_SERVICE).append(url).toString();
    }

    @XmlElement(name = "type")
    public SearchType getType() {
        return type;
    }

    public void setType(SearchType type) {
        this.type = type;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @XmlElement(name = "score")
    public double getScore() {
        return score;
    }

    @XmlElement(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @XmlElement(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlElement(name = "snippet")
    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void addParams(String paramName, String paramValue) {
        this.params.put(paramName, paramValue);
    }

    public long getIndexed() {
        return indexed;
    }

    public void setIndexed(long indexed) {
        this.indexed = indexed;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ResponseItem other = (ResponseItem) obj;
        if ((this.url == null) ? (other.url != null) : !this.url.equals(other.url)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (this.url != null ? this.url.hashCode() : 0);
        return hash;
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
}

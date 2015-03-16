package model.input;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.annotation.Generated;
import java.util.Collection;

/**
 * The input json is parsed to this POJO( internally called Data Transfer Object),
 *
 * @see model.input.SearchRequest
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
public class InputDTO {

    @JsonProperty("query")
    private String query;
    @JsonProperty("lang")
    private String lang;
    @JsonProperty("region")
    private String region;
    @JsonProperty("totalNumber")
    private int totalNumber;
    @JsonProperty("engines")
    private Collection<String> searchEngines;
    @JsonProperty("contents")
    private Collection<ContentDTO> contents;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public Collection<String> getSearchEngines() {
        return searchEngines;
    }

    public void setSearchEngines(Collection<String> searchEngines) {
        this.searchEngines = searchEngines;
    }

    public Collection<ContentDTO> getContents() {
        return contents;
    }

    public void setContents(Collection<ContentDTO> contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "InputDTO{" +
                "query='" + query + '\'' +
                ", lang='" + lang + '\'' +
                ", region='" + region + '\'' +
                ", totalNumber=" + totalNumber +
                ", engines= " + searchEngines +
                ", contents:" + contents +
                '}';
    }
}

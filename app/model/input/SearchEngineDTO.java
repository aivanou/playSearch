package model.input;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
        "linky",
        "google",
        "bing"
})
public class SearchEngineDTO {

    @JsonProperty("linky")
    private String linky;
    @JsonProperty("google")
    private String google;
    @JsonProperty("bing")
    private String bing;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("linky")
    public String getLinky() {
        return linky;
    }

    @JsonProperty("linky")
    public void setLinky(String linky) {
        this.linky = linky;
    }

    @JsonProperty("google")
    public String getGoogle() {
        return google;
    }

    @JsonProperty("google")
    public void setGoogle(String google) {
        this.google = google;
    }

    @JsonProperty("bing")
    public String getBing() {
        return bing;
    }

    @JsonProperty("bing")
    public void setBing(String bing) {
        this.bing = bing;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

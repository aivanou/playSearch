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
    "cat",
    "vid",
    "news",
    "doc",
    "pic"
})
public class ContentTypeDTO {

    @JsonProperty("cat")
    private Integer cat;
    @JsonProperty("vid")
    private Integer vid;
    @JsonProperty("news")
    private Integer news;
    @JsonProperty("doc")
    private Integer doc;
    @JsonProperty("pic")
    private Integer pic;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("cat")
    public Integer getCat() {
        return cat;
    }

    @JsonProperty("cat")
    public void setCat(Integer cat) {
        this.cat = cat;
    }

    @JsonProperty("vid")
    public Integer getVid() {
        return vid;
    }

    @JsonProperty("vid")
    public void setVid(Integer vid) {
        this.vid = vid;
    }

    @JsonProperty("news")
    public Integer getNews() {
        return news;
    }

    @JsonProperty("news")
    public void setNews(Integer news) {
        this.news = news;
    }

    @JsonProperty("doc")
    public Integer getDoc() {
        return doc;
    }

    @JsonProperty("doc")
    public void setDoc(Integer doc) {
        this.doc = doc;
    }

    @JsonProperty("pic")
    public Integer getPic() {
        return pic;
    }

    @JsonProperty("pic")
    public void setPic(Integer pic) {
        this.pic = pic;
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

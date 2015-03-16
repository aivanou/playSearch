package model.input;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.annotation.Generated;

/**
 * The input json for search queries is parsed to this POJO
 *
 * @see model.input.InputDTO uses this class
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
public class ContentDTO {

    @JsonProperty("type")
    private String type;

    @JsonProperty("number")
    private int number;

    @JsonProperty("from")
    private int from;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    @Override
    public String toString() {
        return "ContentDTO{" +
                "type='" + type + '\'' +
                ", number=" + number +
                ", from=" + from +
                '}';
    }
}

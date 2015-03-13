package model;


import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.annotation.Generated;

/**
 * used by services.entities.EntitiesService
 * if there was an error field error has an error object
 * in other case the entity contatins response
 */
@JsonSerialize(include = JsonSerialize.Inclusion.ALWAYS)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
        "message",
        "error"
})
public class EntityResponse<T> {

    @JsonProperty("error")
    private Throwable error;
    @JsonProperty("message")
    private T entity;

    public EntityResponse() {
        this.entity = null;
        this.error = null;
    }

    public EntityResponse(T entity) {
        this.entity = entity;
        this.error = null;
    }

    public EntityResponse(Throwable error) {
        this.error = error;
        this.entity = null;
    }

    public EntityResponse(Throwable error, T message) {
        this.error = error;
        this.entity = message;
    }

    @JsonProperty("error")
    public Throwable getError() {
        return error;
    }

    @JsonProperty("message")
    public T getMessage() {
        return entity;
    }

    @Override
    public String toString() {
        return "EntityResponse{" +
                "entity='" + entity + '\'' +
                '}';
    }
}

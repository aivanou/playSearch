package model;

/**
 * Represents engines to which the requests will be made
 * Currently suprorted: [elastic, google, bing]
 * The elastic is the @see model.request.InternalContentRequest type
 * and [google, bing] are @see model.request.ExternalContentRequest
 */
public class SearchEngineType {

    private String name;

    public SearchEngineType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchEngineType that = (SearchEngineType) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "SearchEngineType{" +
                "name='" + name + '\'' +
                '}';
    }
}

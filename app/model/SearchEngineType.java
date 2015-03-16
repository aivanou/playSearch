package model;

/**
 * The search engine types,
 * Linky is the company developed engine,
 * Google and Bing are called through the proxies
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

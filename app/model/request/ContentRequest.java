package model.request;

/**
 * The general content request,
 *
 * @see model.request.ExternalContentRequest for more information
 * @see model.request.InternalContentRequest for more information
 */
public class ContentRequest {

    protected String query;
    protected int number;
    protected int from;

    public ContentRequest(String query, int number, int from) {
        this.query = query;
        this.number = number;
        this.from = from;
    }

    public String getQuery() {
        return query;
    }

    public int getNumber() {
        return number;
    }

    public int getFrom() {
        return from;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContentRequest that = (ContentRequest) o;

        if (from != that.from) return false;
        if (number != that.number) return false;
        if (query != null ? !query.equals(that.query) : that.query != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = query != null ? query.hashCode() : 0;
        result = 31 * result + number;
        result = 31 * result + from;
        return result;
    }

    @Override
    public String toString() {
        return "ContentRequest{" +
                "query='" + query + '\'' +
                ", number=" + number +
                ", from=" + from +
                '}';
    }
}

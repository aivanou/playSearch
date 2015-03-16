package model.request;

/**
 * Represents the request to the external services like Bing or Google
 *
 * @see services.search.provider.impl.BingApiSearchProvider for usage
 * @see services.search.provider.impl.GoogleApiSearchProvider for usage
 */
public class ExternalContentRequest extends ContentRequest {

    private String charset;
    private String lang;
    private String region;

    public ExternalContentRequest(String query, int number, int from, String charset, String lang, String region) {
        super(query, number, from);
        this.charset = charset;
        this.lang = lang;
        this.region = region;
    }

    public String getCharset() {
        return charset;
    }

    public String getLang() {
        return lang;
    }

    public String getRegion() {
        return region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ExternalContentRequest that = (ExternalContentRequest) o;

        if (charset != null ? !charset.equals(that.charset) : that.charset != null) return false;
        if (lang != null ? !lang.equals(that.lang) : that.lang != null) return false;
        if (region != null ? !region.equals(that.region) : that.region != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (charset != null ? charset.hashCode() : 0);
        result = 31 * result + (lang != null ? lang.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ExternalContentRequest{" +
                "charset='" + charset + '\'' +
                ", lang='" + lang + '\'' +
                ", region='" + region + '\'' +
                '}';
    }
}

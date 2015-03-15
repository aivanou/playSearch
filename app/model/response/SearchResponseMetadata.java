package model.response;

/**
 * The metadata information
 */
public class SearchResponseMetadata {

    private long queryTime;
    private long start;
    private long finish;

    public SearchResponseMetadata(long queryTime, long start, long finish) {
        this.queryTime = queryTime;
        this.start = start;
        this.finish = finish;
    }

    public long getQueryTime() {
        return queryTime;
    }

    public long getStart() {
        return start;
    }

    public long getFinish() {
        return finish;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchResponseMetadata that = (SearchResponseMetadata) o;

        if (finish != that.finish) return false;
        if (queryTime != that.queryTime) return false;
        if (start != that.start) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (queryTime ^ (queryTime >>> 32));
        result = 31 * result + (int) (start ^ (start >>> 32));
        result = 31 * result + (int) (finish ^ (finish >>> 32));
        return result;
    }
}

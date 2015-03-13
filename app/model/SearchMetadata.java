package model;

/**used by @see SearchResponse ;
 * it saves here information about elasticsearch query execution
 * like query time, size, etc.
 * */
public class SearchMetadata {

    private int totalResults;
    private int queryTime;
    private String query;
    private int querySize;

    public SearchMetadata() {
    }

    public SearchMetadata(String query, int totalResults, int queryTime, int querySize) {
        this.totalResults = totalResults;
        this.queryTime = queryTime;
        this.query = query;
        this.querySize = querySize;
    }

    public int getQuerySize() {
        return querySize;
    }

    public void setQuerySize(int querySize) {
        this.querySize = querySize;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(int queryTime) {
        this.queryTime = queryTime;
    }
}

package services.statistics.impl;

import model.SearchRequest;

import java.util.List;

public class QueryStatistics {

    public static fromSearchRequest(SearchRequest request) {

    }

    private long timestamp;
    private String query;
    private String lang;
    private int totalNumber;
    private List<String> engines;
    private List<String> contentRequests;

    private QueryStatistics(long timestamp, String query, String lang, int totalNumber, List<String> engines, List<String> contentRequests) {
        this.timestamp = timestamp;
        this.query = query;
        this.lang = lang;
        this.totalNumber = totalNumber;
        this.engines = engines;
        this.contentRequests = contentRequests;
    }

    public String getQuery() {
        return query;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getLang() {
        return lang;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public List<String> getEngines() {
        return engines;
    }

    public List<String> getContentRequests() {
        return contentRequests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueryStatistics that = (QueryStatistics) o;

        if (timestamp != that.timestamp) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (timestamp ^ (timestamp >>> 32));
    }
}

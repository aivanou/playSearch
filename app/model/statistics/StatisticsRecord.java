package model.statistics;

public class StatisticsRecord {
    String query;
    long time;

    public StatisticsRecord(String query, long time) {
        this.query = query;
        this.time = time;
    }

    public String getQuery() {
        return query;
    }

    public long getTime() {
        return time;
    }
}

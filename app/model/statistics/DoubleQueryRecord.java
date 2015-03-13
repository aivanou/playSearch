package model.statistics;


/**
 * Represents the entity for writing data about first-second requests
 * */
public class DoubleQueryRecord {

    private int executedTimes;
    private String query;
    private int firstQueryTime;
    private int secondQueryTime;
    private int firstQuerySize;
    private int secondQuerySize;

    public DoubleQueryRecord() {
        this.firstQueryTime = 0;
        this.secondQueryTime = 0;
        this.executedTimes = 0;
        this.firstQuerySize = 0;
        this.secondQuerySize = 0;
    }

    public DoubleQueryRecord(String query, int firstQueryTime, int firstQuerySize) {
        this.query = query;
        this.firstQueryTime = firstQueryTime;
        this.secondQueryTime = 0;
        this.executedTimes = 1;
        this.firstQuerySize = firstQuerySize;
    }

    public DoubleQueryRecord(String query, int firstQueryTime, int firstQuerySize, int secondQueryTime, int secondQuerySize) {
        this.query = query;
        this.firstQueryTime = firstQueryTime;
        this.secondQueryTime = secondQueryTime;
        this.executedTimes = 2;
        this.firstQuerySize = firstQuerySize;
        this.secondQuerySize = secondQuerySize;
    }

    public int getFirstQuerySize() {
        return firstQuerySize;
    }

    public void setFirstQuerySize(int firstQuerySize) {
        this.firstQuerySize = firstQuerySize;
    }

    public int getSecondQuerySize() {
        return secondQuerySize;
    }

    public void setSecondQuerySize(int secondQuerySize) {
        this.secondQuerySize = secondQuerySize;
    }

    public int getFirstQueryTime() {
        return firstQueryTime;
    }

    public void setFirstQueryTime(int queryTime) {
        firstQueryTime = queryTime;
    }

    public int getSecondQueryTime() {
        return secondQueryTime;
    }

    public void setSecondQueryTime(int queryTime) {
        secondQueryTime = queryTime;
    }

    public int getExecutedTimes() {
        return executedTimes;
    }

    public String getQuery() {
        return query;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoubleQueryRecord that = (DoubleQueryRecord) o;

        return query.equals(that.query);

    }

    @Override
    public int hashCode() {
        return query.hashCode();
    }
}

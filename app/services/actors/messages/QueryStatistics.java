package services.actors.messages;

import model.Writable;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * The message for @see services.actors.StatisticsActor
 */
public class QueryStatistics implements Serializable, Writable {

    //TODO: add id

    public static QueryStatistics fromSearchRequest(String json) {
        return new QueryStatistics(System.nanoTime(), json);
    }

    private long timestamp;
    private String query;

    public QueryStatistics(long timestamp, String query) {
        this.timestamp = timestamp;
        this.query = query;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getQuery() {
        return query;
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        DataOutputStream stream = new DataOutputStream(out);
        stream.writeLong(timestamp);
        stream.writeChars(query);
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

    @Override
    public String toString() {
        return "QueryStatistics{" +
                "timestamp=" + timestamp +
                ", query='" + query + '\'' +
                '}';
    }
}

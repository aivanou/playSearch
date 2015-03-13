package datasources.dao.statistics;

import java.util.List;

public interface StatisticsDAO<T> {

    /**
     * Inserts entities into some storage as an atomic operation
     *
     * @param entities - List of Input type
     */
    void insertBatch(List<T> entities);
}

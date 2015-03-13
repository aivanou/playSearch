package datasources.dao.statistics.impl;

import play.Logger;
import play.Play;
import model.statistics.DoubleQueryRecord;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Writes Statistics about double queries into SQL data base
 * table name can be configured in ok.statistics.groups.table
 * table script: conf/schema/db/statistics.sql
 */
public class DoubleQuerySqlDAOImpl extends SqlDAO<DoubleQueryRecord> {

    public DoubleQuerySqlDAOImpl() {
        INSERT_QUERY = "INSERT " + Play.application().configuration().getString("ok.statistics.groups.table")
                + " (id, query, executedTimes, firstQueryTime, secondQueryTime, firstQuerySize, secondQuerySize) VALUES(0,?,?,?,?,?,?)";
        Logger.debug("DoubleQuerySqlDAOImpl: " + INSERT_QUERY);
    }

    @Override
    protected void setPrepareStatement(PreparedStatement ps, DoubleQueryRecord entity) throws SQLException {
        ps.setString(1, entity.getQuery());
        ps.setInt(2, entity.getExecutedTimes());
        ps.setInt(3, entity.getFirstQueryTime());
        ps.setInt(4, entity.getSecondQueryTime());
        ps.setInt(5, entity.getFirstQuerySize());
        ps.setInt(6, entity.getSecondQuerySize());
    }
}

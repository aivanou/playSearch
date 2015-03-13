package datasources.dao.statistics.impl;

import play.Logger;
import play.Play;
import model.statistics.StatisticsRecord;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Writes statistics about each query into SQL database
 * table name can be configured in ok.statistics.table
 * table script: conf/schema/db/statistics.sql
 */
public class QuerySqlDAOImpl extends SqlDAO<StatisticsRecord> {


    public QuerySqlDAOImpl() {
        INSERT_QUERY = "INSERT " + Play.application().configuration().getString("ok.statistics.table") + " (id, query, date) VALUES(0,?,?)";
        Logger.debug("QuerySqlDAOImpl: " + INSERT_QUERY);
    }

    @Override
    protected void setPrepareStatement(PreparedStatement ps, StatisticsRecord entity) throws SQLException {
        ps.setString(1, entity.getQuery());
        ps.setLong(2, entity.getTime());
    }

}

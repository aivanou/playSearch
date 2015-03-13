package datasources.dao.statistics.impl;

import play.Logger;
import play.db.DB;
import datasources.dao.statistics.StatisticsDAO;
import util.CloseUtil;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Simple Sql implementation
 */
public abstract class SqlDAO<T> implements StatisticsDAO<T> {

    protected String INSERT_QUERY;

    @Override
    public void insertBatch(List<T> data) {
        PreparedStatement ps = null;
        Connection connection = null;
        try {
            connection = DB.getConnection("statistics", false);
            ps = connection.prepareStatement(INSERT_QUERY);
            for (T aData : data) {
                setPrepareStatement(ps, aData);
                ps.addBatch();
            }
            int[] updateCounts = ps.executeBatch();
            Logger.debug("Query Statistics records were were inserted. Size: " + updateCounts.length);
            connection.commit();
        } catch (BatchUpdateException e) {
            try {
                connection.rollback();
                Logger.error("MySQLStatisticDAL:insertBatch() can't insertBatch : " + e.getMessage(), e);
            } catch (Exception ex) {
                Logger.error("Insert can't rollback in mysql because exception in insertBatch : " + ex.getMessage(), ex);
            }
        } catch (SQLException e) {
            Logger.error(" in method insertBatch : " + e.getMessage(), e);
        } finally {
            CloseUtil.close(ps);
            CloseUtil.close(connection);
        }
    }

    protected abstract void setPrepareStatement(PreparedStatement ps, T entity) throws SQLException;

}

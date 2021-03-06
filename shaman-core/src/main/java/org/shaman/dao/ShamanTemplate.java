package org.shaman.dao;

import org.shaman.dao.setter.InsertBatchSetter;
import org.shaman.dao.setter.InsertSetter;
import org.shaman.dao.setter.UpdateBatchSetter;
import org.shaman.dao.setter.UpdateSetter;
import org.shaman.dao.vo.*;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by fenglei on 2016/3/2.
 */
public class ShamanTemplate extends JdbcTemplate {

    /**
     * query query
     *
     * @param sql
     * @param args
     * @param clazz
     * @param <T>
     * @return
     * @throws DataAccessException
     */
    public <T> List<T> query(String sql, Object[] args, Class clazz) throws DataAccessException {
        org.shaman.dao.ObjectRowMapper objectRowMapper = new org.shaman.dao.ObjectRowMapper(clazz);
        return super.query(sql, args, objectRowMapper);
    }

    /**
     * updateTable updateTable
     *
     * @param sqlUpdateVo
     */
    public void updateTable(SQLUpdateVo sqlUpdateVo) {
        String sql = sqlUpdateVo.getSql();
        Map<Field, Object> sqlSetMap = sqlUpdateVo.getSqlSetMap();
        UpdateSetter updateSetter = new UpdateSetter(sqlSetMap);
        super.update(sql, updateSetter);
    }

    /**
     * insertTable insertTable
     *
     * @param sqlInsertVo
     */
    public void insert(SQLInsertVo sqlInsertVo) {
        String sql = sqlInsertVo.getSql();
        Map<Field, Object> sqlSetMap = sqlInsertVo.getSqlSetMap();
        InsertSetter insertSetter = new InsertSetter(sqlSetMap);
        super.update(sql, insertSetter);
    }

    /**
     * insertBatch insertBatch
     *
     * @param sqlBatchVo
     */
    public void insertBatch(SQLBatchVo sqlBatchVo) {
        String sql = sqlBatchVo.getSql();
        List<Map<Field, Object>> sqlSetList = sqlBatchVo.getSqlSetList();
        InsertBatchSetter insertBatchSetter = new InsertBatchSetter(sqlSetList);
        super.batchUpdate(sql, insertBatchSetter);
    }

    /**
     * updateBatch updateBatch
     *
     * @param sqlBatchVo
     */
    public void updateBatch(SQLBatchVo sqlBatchVo) {
        String sql = sqlBatchVo.getSql();
        List<Map<Field, Object>> sqlSetList = sqlBatchVo.getSqlSetList();
        UpdateBatchSetter updateBatchSetter = new UpdateBatchSetter(sqlSetList);
        super.batchUpdate(sql, updateBatchSetter);
    }

    /**
     * deleteRowForTable deleteRowForTable
     *
     * @param deleteSQL
     */
    public void delete(String deleteSQL) {
        super.update(deleteSQL);
    }

    /**
     * queryForTable queryForTable
     *
     * @param sqlSelectVo
     * @return
     */
    public <T> List<T> queryListForTable(SQLSelectVo sqlSelectVo) {
        String sql = sqlSelectVo.getSql();
        Class clazz = sqlSelectVo.getTableClazz();
        Object[] args = sqlSelectVo.getArgList().toArray();
        List rsList = this.query(sql, args, clazz);
        return rsList;
    }

    /**
     * queryObjectForTable queryObjectForTable
     *
     * @param sqlSelectVo
     * @return
     */
    public <T> T queryObjectForTable(SQLSelectVo sqlSelectVo) {
        String sql = sqlSelectVo.getSql();
        Class<T> clazz = sqlSelectVo.getTableClazz();
        Object[] args = sqlSelectVo.getArgList().toArray();
        List<T> list = this.query(sql, args, clazz);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * requiredSingleResult requiredSingleResult
     *
     * @param results
     * @param <T>
     * @return
     * @throws IncorrectResultSizeDataAccessException
     */
    public static <T> T requiredSingleResult(Collection<T> results) throws IncorrectResultSizeDataAccessException {
        int size = (results != null ? results.size() : 0);
        if (size == 0) {
            return null;
        }
        if (results.size() > 1) {
            throw new IncorrectResultSizeDataAccessException(1, size);
        }
        return results.iterator().next();
    }

    /**
     * queryForLong queryForLong
     *
     * @param sqlSelectVo
     * @return
     */
    public long queryForLong(SQLSelectVo sqlSelectVo) {
        String sql = sqlSelectVo.getSql();
        Object[] args = sqlSelectVo.getArgList().toArray();
        return super.queryForObject(sql, args, Long.class);
    }

    /**
     * queryForLong queryForLong
     *
     * @param sqlSelectVo
     * @return
     */
    public int queryForInt(SQLSelectVo sqlSelectVo) {
        String sql = sqlSelectVo.getSql();
        Object[] args = sqlSelectVo.getArgList().toArray();
        return super.queryForObject(sql, args, Integer.class);
    }

}

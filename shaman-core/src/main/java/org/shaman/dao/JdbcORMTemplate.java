package org.shaman.dao;

import org.shaman.dao.vo.SQLInsertVo;
import org.shaman.dao.vo.SQLSelectVo;
import org.shaman.dao.vo.SQLUpdateVo;
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
public class JdbcORMTemplate extends JdbcTemplate {

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
        final Map<Field, Object> sqlSetMap = sqlUpdateVo.getSqlSetMap();
        final Map<Field, Object> sqlWhereMap = sqlUpdateVo.getSqlWhereMap();
        org.shaman.dao.UpdateSetter updateSetter = new org.shaman.dao.UpdateSetter(sqlSetMap, sqlWhereMap);
        super.update(sql, updateSetter);
    }

    /**
     * updateTableBatch updateTableBatch
     *
     * @param objList
     * @param clazz
     */
    public <T> void updateTableBatch(List<T> objList, Class clazz) {

    }

    /**
     * insertTable insertTable
     *
     * @param sqlInsertVo
     */
    public void insertTable(SQLInsertVo sqlInsertVo) {
        String sql = sqlInsertVo.getSql();
        final Map<Field, Object> sqlSetMap = sqlInsertVo.getSqlSetMap();
        InsertSetter insertSetter = new InsertSetter(sqlSetMap);
        super.update(sql, insertSetter);
    }

    public <T> void insertTableBatch(List<T> objList) {

    }

    /**
     * deleteRowForTable deleteRowForTable
     *
     * @param deleteSQL
     * @param <T>
     */
    public <T> void deleteRowForTable(String deleteSQL) {
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
//        T obj = this.queryForObject(sql, args, clazz);
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
        return super.queryForLong(sql, args);
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
        return super.queryForInt(sql, args);
    }

    /**
     * queryForLong queryForLong
     *
     * @param sql
     * @param args
     * @return
     */
    public int queryForInt(String sql, Object[] args) {
        return super.queryForInt(sql, args);
    }
}

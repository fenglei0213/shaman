package org.shaman.dao;

import org.shaman.dao.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by fenglei on 2016/3/3.
 */
public class JdbcORMDao {

    private JdbcORMTemplate jdbcORMTemplate;

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
        return jdbcORMTemplate.query(sql, args, clazz);
    }

    /**
     * queryForCountInt queryForCountInt
     *
     * @param queryCountVo
     * @return
     */
    public int queryForCountInt(QueryCountVo queryCountVo) {
        SQLSelectVo sqlSelectVo = SQLBuilder.buildSelectCountTableSQL(queryCountVo);
        return jdbcORMTemplate.queryForInt(sqlSelectVo);
    }

    /**
     * queryForCountInt queryForCountInt
     *
     * @param sql
     * @return
     */
    public int queryForCountInt(String sql,Object[] args) {
        return jdbcORMTemplate.queryForInt(sql,args);
    }

    /**
     * queryForCountLong queryForCountLong
     *
     * @param queryCountVo
     * @return
     */
    public long queryForCountLong(QueryCountVo queryCountVo) {
        SQLSelectVo sqlSelectVo = SQLBuilder.buildSelectCountTableSQL(queryCountVo);
        return jdbcORMTemplate.queryForLong(sqlSelectVo);
    }

    /**
     * queryForTable queryForTable
     *
     * @param sql
     * @param args
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> queryList(String sql, Object[] args, Class clazz) {
        return jdbcORMTemplate.query(sql, args, clazz);
    }

    /**
     * queryForTable queryForTable
     *
     * @param queryVo
     * @param <T>
     * @return
     */
    public <T> List<T> queryListForTable(QueryVo<T> queryVo) {
        SQLSelectVo<T> sqlSelectVo = SQLBuilder.buildSelectTableSQL(queryVo);
        return jdbcORMTemplate.queryListForTable(sqlSelectVo);
    }

    /**
     * @param queryVo
     * @param <T>
     * @return
     */
    public <T> T queryObjectForTable(QueryVo<T> queryVo) {
        SQLSelectVo<T> sqlSelectVo = SQLBuilder.buildSelectTableSQL(queryVo);
        return jdbcORMTemplate.queryObjectForTable(sqlSelectVo);
    }

    /**
     * updateObject2Table updateObject2Table
     *
     * @param obj
     * @param <T>
     */
    public <T> void updateObjectForTable(T obj) {
        SQLUpdateVo sqlUpdateVo = SQLBuilder.buildUpdateTableSQL(obj);
        jdbcORMTemplate.updateTable(sqlUpdateVo);
    }

    /**
     * addObject2Table addObject2Table
     *
     * @param obj
     * @param <T>
     */
    public <T> void insertObjectForTable(T obj) {
        SQLInsertVo sqlInsertVo = SQLBuilder.buildInsertTableSQL(obj);
        jdbcORMTemplate.insertTable(sqlInsertVo);
    }

    /**
     * deleteRowForTable deleteRowForTable
     *
     * @param deleteVo
     */
    public void deleteRowForTable(DeleteVo deleteVo) {
        String deleteSQL = SQLBuilder.buildDeleteTableSQL(deleteVo);
        jdbcORMTemplate.deleteRowForTable(deleteSQL);
    }

    public void setJdbcORMTemplate(JdbcORMTemplate jdbcORMTemplate) {
        this.jdbcORMTemplate = jdbcORMTemplate;
    }
}

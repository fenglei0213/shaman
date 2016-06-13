package org.shaman.dao;

import org.shaman.util.ReflectionUtils;
import org.springframework.dao.DataAccessException;
import org.shaman.dao.vo.*;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by fenglei on 2016/3/3.
 */
public class ShamanDao {

    private ShamanTemplate shamanTemplate;

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
        return shamanTemplate.query(sql, args, clazz);
    }

    /**
     * queryForCountInt queryForCountInt
     *
     * @param queryCountVo
     * @return
     */
    public int queryForCountInt(QueryCountVo queryCountVo) {
        SQLSelectVo sqlSelectVo = SQLBuilder.buildSelectCountTableSQL(queryCountVo);
        return shamanTemplate.queryForInt(sqlSelectVo);
    }

    /**
     * queryForCountLong queryForCountLong
     *
     * @param queryCountVo
     * @return
     */
    public long queryForCountLong(QueryCountVo queryCountVo) {
        SQLSelectVo sqlSelectVo = SQLBuilder.buildSelectCountTableSQL(queryCountVo);
        return shamanTemplate.queryForLong(sqlSelectVo);
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
        return shamanTemplate.query(sql, args, clazz);
    }

    /**
     * queryForTable queryForTable
     *
     * @param queryVo
     * @param <T>
     * @return
     */
    public <T> List<T> queryList(QueryVo<T> queryVo) {
//        Class tableClazz = ReflectionUtils.getGenericClass(queryVo);
//        queryVo.setTableClazz(tableClazz);
        SQLSelectVo<T> sqlSelectVo = SQLBuilder.buildSelectTableSQL(queryVo);
        return shamanTemplate.queryListForTable(sqlSelectVo);
    }

    /**
     * @param queryVo
     * @param <T>
     * @return
     */
    public <T> T queryObject(QueryVo<T> queryVo) {
        SQLSelectVo<T> sqlSelectVo = SQLBuilder.buildSelectTableSQL(queryVo);
        return shamanTemplate.queryObjectForTable(sqlSelectVo);
    }

    /**
     * updateObject2Table updateObject2Table
     *
     * @param obj
     * @param <T>
     */
    public <T> void updateObjectForTable(T obj) {
        SQLUpdateVo sqlUpdateVo = SQLBuilder.buildUpdateTableSQL(obj);
        shamanTemplate.updateTable(sqlUpdateVo);
    }

    /**
     * addObject2Table addObject2Table
     *
     * @param obj
     * @param <T>
     */
    public <T> void insertObject(T obj) {
        SQLInsertVo sqlInsertVo = SQLBuilder.buildInsertTableSQL(obj);
        shamanTemplate.insert(sqlInsertVo);
    }

    /**
     * insertObjectBatch insertObjectBatch
     * @param objectList
     * @param <T>
     */
    public <T> void insertObjectBatch(List<T> objectList) {
        if(CollectionUtils.isEmpty(objectList)){
            return;
        }
        SQLInsertBatchVo sqlInsertBatchVo = SQLBuilder.buildInsertBatchTableSQL(objectList);
        shamanTemplate.insertBatch(sqlInsertBatchVo);
    }

    /**
     * deleteRowForTable deleteRowForTable
     *
     * @param deleteVo
     */
    public void deleteRow(DeleteVo deleteVo) {
        String deleteSQL = SQLBuilder.buildDeleteTableSQL(deleteVo);
        shamanTemplate.delete(deleteSQL);
    }

    public void setShamanTemplate(ShamanTemplate shamanTemplate) {
        this.shamanTemplate = shamanTemplate;
    }


}
package org.shaman.dao;

import org.shaman.exception.ShamanArgsException;
import org.springframework.dao.DataAccessException;
import org.shaman.dao.vo.*;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by fenglei on 2016/3/3.
 */
public class ShamanDao {

    private ShamanTemplate shamanTemplate;

    /**
     * execute execute
     *
     * @param sql
     */
    public void execute(String sql) {
        shamanTemplate.execute(sql);
    }

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
     * @param queryVo
     * @return
     */
    public int queryForCountInt(QueryVo queryVo) {
        SQLSelectVo sqlSelectVo = SQLBuilder.buildSelectTableSQL(queryVo);
        return shamanTemplate.queryForInt(sqlSelectVo);
    }

    /**
     * queryForCountLong queryForCountLong
     *
     * @param queryVo
     * @return
     */
    public long queryForCountLong(QueryVo queryVo) {
        SQLSelectVo sqlSelectVo = SQLBuilder.buildSelectTableSQL(queryVo);
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
        this.updateObjectForTable(obj, null, null);
    }

    /**
     * updateObject2Table updateObject2Table
     *
     * @param obj
     * @param sqlWhereCusSet
     * @param <T>
     */
    public <T> void updateObjectForTable(T obj, Set<String> sqlWhereCusSet) {
        this.updateObjectForTable(obj, sqlWhereCusSet, null);
    }

    /**
     * updateObject2Table updateObject2Table
     *
     * @param obj
     * @param sqlWhereCusSet
     * @param tableName
     * @param <T>
     */
    public <T> void updateObjectForTable(T obj, Set<String> sqlWhereCusSet, String tableName) {
        SQLUpdateVo sqlUpdateVo = SQLBuilder.buildUpdateBatchTableSQL(obj, sqlWhereCusSet, tableName);
        shamanTemplate.updateTable(sqlUpdateVo);
    }

    /**
     * addObject2Table addObject2Table
     *
     * @param obj
     * @param <T>
     */
    public <T> void insertObject(T obj) {
        this.insertObject(obj, null);
    }

    /**
     * addObject2Table addObject2Table
     *
     * @param obj
     * @param tableName
     * @param <T>
     */
    public <T> void insertObject(T obj, String tableName) {
        SQLInsertVo sqlInsertVo = SQLBuilder.buildInsertTableSQL(obj, tableName);
        shamanTemplate.insert(sqlInsertVo);
    }

    /**
     * insertObjectBatch insertObjectBatch
     *
     * @param objectList
     * @param <T>
     */
    public <T> void insertBatch(List<T> objectList) {
        this.insertBatch(objectList, null);
    }

    /**
     * insertObjectBatch insertObjectBatch
     *
     * @param objectList
     * @param tableName
     * @param <T>
     */
    public <T> void insertBatch(List<T> objectList, String tableName) {
        if (CollectionUtils.isEmpty(objectList)) {
            return;
        }
        // List is not empty,But each item is empty
        for (T item : objectList) {
            if (item == null) {
                throw new ShamanArgsException("insertBatch args objectList contains NULL item");
            }
        }
        SQLBatchVo sqlBatchVo = SQLBuilder.buildInsertBatchTableSQL(objectList, tableName);
        shamanTemplate.insertBatch(sqlBatchVo);
    }

    /**
     * updateBatch updateBatch
     * update key default primary key
     *
     * @param objectList
     * @param <T>
     */
    public <T> void updateBatch(List<T> objectList) {
        this.updateBatch(objectList, null);
    }

    /**
     * updateBatch updateBatch
     * update key default primary key
     *
     * @param objectList
     * @param sqlWhereCusSet
     * @param <T>
     */
    public <T> void updateBatch(List<T> objectList, Set<String> sqlWhereCusSet) {
        this.updateBatch(objectList, sqlWhereCusSet, null);
    }

    /**
     * updateBatch updateBatch
     * Custmer Where Key/Value
     *
     * @param objectList
     * @param sqlWhereCusSet
     * @param tableName
     * @param <T>
     */
    public <T> void updateBatch(List<T> objectList, Set<String> sqlWhereCusSet, String tableName) {
        if (CollectionUtils.isEmpty(objectList)) {
            return;
        }
        // List is not empty,But each item is empty
        for (T item : objectList) {
            if (item == null) {
                throw new ShamanArgsException("updateBatch args objectList contains NULL item");
            }
        }
        SQLBatchVo sqlBatchVo = SQLBuilder.buildUpdateBatchTableSQL(objectList, sqlWhereCusSet, tableName);
        shamanTemplate.updateBatch(sqlBatchVo);
    }

    /**
     * replaceBatch replaceBatch
     *
     * @param objectList
     * @param <T>
     */
    public <T> void replaceBatch(List<T> objectList){
        this.replaceBatch(objectList, null);
    }

    /**
     * replaceBatch replaceBatch
     *
     * @param objectList
     * @param tableName
     * @param <T>
     */
    public <T> void replaceBatch(List<T> objectList,String tableName){
        if (CollectionUtils.isEmpty(objectList)) {
            return;
        }
        // List is not empty,But each item is empty
        for (T item : objectList) {
            if (item == null) {
                throw new ShamanArgsException("replaceBatch args objectList contains NULL item");
            }
        }
        SQLBatchVo sqlBatchVo = SQLBuilder.buildReplaceBatchTableSQL(objectList, tableName);
        shamanTemplate.insertBatch(sqlBatchVo);
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

    /**
     * dropTable dropTable
     *
     * @param tableName
     */
    public void dropTable(String tableName) {
        String dropTableSQL = SQLBuilder.buildDropTableSQL(tableName);
        shamanTemplate.execute(dropTableSQL);
    }

    /**
     * truncateTable truncateTable
     *
     * @param tableName
     */
    public void truncateTable(String tableName) {
        String truncateTableSQL = SQLBuilder.buildTruncateTableSQL(tableName);
        shamanTemplate.execute(truncateTableSQL);
    }

    public void setShamanTemplate(ShamanTemplate shamanTemplate) {
        this.shamanTemplate = shamanTemplate;
    }


}

package org.shaman.dao.vo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fenglei on 2016/3/3.
 */
public class QueryVo<T> extends BaseVo<T> {

    private List<String> selectColumnList = Lists.newArrayList();

    private Map<String, Object> whereColumnMap = Maps.newLinkedHashMap();

    private Map<String, List<Number>> whereColumnInMap = Maps.newLinkedHashMap();

    private Map<String, Integer> distinctColumnMap = Maps.newHashMap();

    public List<String> getSelectColumnList() {
        return selectColumnList;
    }

    private QueryVo() {

    }

    public QueryVo(Class tableClazz) {
        super.setTableClazz(tableClazz);
    }

    /**
     * setSelectColumnList setSelectColumnList
     *
     * @return
     */
    public QueryVo setSelectColumnList(String... args) {
        this.selectColumnList = Arrays.asList(args);
        return this;
    }

    public QueryVo setSelectColumnList(List<String> selectColumnList) {
        this.selectColumnList = selectColumnList;
        return this;
    }

    public Map<String, Object> getWhereColumnMap() {
        return whereColumnMap;
    }

    /**
     * setWhereColumnMap setWhereColumnMap
     *
     * @param whereColumnMap
     * @param <T>
     */
    public <T extends LinkedHashMap<String, Object>> QueryVo setWhereColumnMap(T whereColumnMap) {
        this.whereColumnMap = whereColumnMap;
        return this;
    }

    /**
     * setWhereColumn setWhereColumn
     *
     * @param key
     * @param value
     * @return
     */
    public QueryVo setWhereColumnMap(String key, Object value) {
        this.whereColumnMap.put(key, value);
        return this;
    }

    /**
     * setWhereColumn setWhereColumn
     *
     * @param key
     * @param inList
     * @return
     */
    public QueryVo setWhereColumnINMap(String key, List<Number> inList) {
        this.whereColumnInMap.put(key, inList);
        return this;
    }

    public Map<String, Integer> getDistinctColumnMap() {
        return distinctColumnMap;
    }

    /**
     * setDistinctColumnMap setDistinctColumnMap
     *
     * @param distinctColumnMap
     * @param <T>
     */
    public <T extends LinkedHashMap<String, Integer>> QueryVo setDistinctColumnMap(T distinctColumnMap) {
        this.distinctColumnMap = distinctColumnMap;
        return this;
    }

    public Map<String, List<Number>> getWhereColumnInMap() {
        return whereColumnInMap;
    }

    public void setWhereColumnInMap(Map<String, List<Number>> whereColumnInMap) {
        this.whereColumnInMap = whereColumnInMap;
    }
}

package org.shaman.dao.vo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.ImmutablePair;

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

    private Map<String, List<Object>> whereColumnInMap = Maps.newLinkedHashMap();

    private Map<String, Integer> distinctColumnMap = Maps.newHashMap();

    private ImmutablePair<Integer, Integer> limitPair;

    private String countColumnName = "";

    private String countDistinctColumnName = "";

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
    public QueryVo addColumn(String... args) {
        this.selectColumnList = Arrays.asList(args);
        return this;
    }

    /**
     * addCountColumn addCountColumn
     *
     * @param countColumnName
     * @return
     */
    public QueryVo addCountColumn(String countColumnName) {
        this.setCountColumnName(countColumnName);
        return this;
    }

    /**
     * addCountDistinctColumn addCountDistinctColumn
     *
     * @param countDistinctColumnName
     * @return
     */
    public QueryVo addCountDistinctColumn(String countDistinctColumnName) {
        this.setCountColumnName(countDistinctColumnName);
        return this;
    }

    /**
     * addColumnList addColumnList
     *
     * @param selectColumnList
     * @return
     */
    public QueryVo addColumnList(List<String> selectColumnList) {
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
    public <T extends LinkedHashMap<String, Object>> QueryVo addConditionMap(T whereColumnMap) {
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
    public QueryVo addCondition(String key, Object value) {
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
    public QueryVo addInCondition(String key, List<Object> inList) {
        this.whereColumnInMap.put(key, inList);
        return this;
    }

    /**
     * setWhereColumn setWhereColumn
     *
     * @param key
     * @param objects
     * @return
     */
    public QueryVo addInCondition(String key, Object... objects) {
        this.whereColumnInMap.put(key, Arrays.asList(objects));
        return this;
    }

    /**
     * addLimit addLimit
     *
     * @param startOffset
     * @param endOffset
     * @return
     */
    public QueryVo addLimit(int startOffset, int endOffset) {
        limitPair = new ImmutablePair<Integer, Integer>(startOffset, endOffset);
        return this;
    }

    /**
     * addLimit addLimit
     *
     * @param endOffset
     * @return
     */
    public QueryVo addLimit(int endOffset) {
        limitPair = new ImmutablePair<Integer, Integer>(0, endOffset);
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

    public Map<String, List<Object>> getWhereColumnInMap() {
        return whereColumnInMap;
    }

    public void setWhereColumnInMap(Map<String, List<Object>> whereColumnInMap) {
        this.whereColumnInMap = whereColumnInMap;
    }

    public ImmutablePair<Integer, Integer> getLimitPair() {
        return limitPair;
    }

    public void setLimitPair(ImmutablePair<Integer, Integer> limitPair) {
        this.limitPair = limitPair;
    }

    public String getCountColumnName() {
        return countColumnName;
    }

    public void setCountColumnName(String countColumnName) {
        this.countColumnName = countColumnName;
    }

    public String getCountDistinctColumnName() {
        return countDistinctColumnName;
    }

    public void setCountDistinctColumnName(String countDistinctColumnName) {
        this.countDistinctColumnName = countDistinctColumnName;
    }
}

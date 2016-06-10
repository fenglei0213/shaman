package org.shaman.dao.vo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by fenglei on 2016/3/3.
 */
public class QueryVo<T> extends BaseVo {

    private List<String> selectColumnList = Lists.newArrayList();

    private Map<String, Object> whereColumnMap = Maps.newLinkedHashMap();

    private Map<String, Integer> distinctColumnMap = Maps.newHashMap();

    public List<String> getSelectColumnList() {
        return selectColumnList;
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
    public <T extends TreeMap<String, Object>> QueryVo setWhereColumnMap(T whereColumnMap) {
        this.whereColumnMap = whereColumnMap;
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
    public <T extends TreeMap<String, Integer>> QueryVo setDistinctColumnMap(T distinctColumnMap) {
        this.distinctColumnMap = distinctColumnMap;
        return this;
    }
}

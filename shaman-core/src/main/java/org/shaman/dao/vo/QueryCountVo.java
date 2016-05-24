package org.shaman.dao.vo;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by fenglei on 2016/3/7.
 */
public class QueryCountVo extends BaseVo {

    public static final String COLUMN_ALL="*";

    /**
     * if COUNT(*),set this field COLUMN_ALL
     *
     */
    private String countColumnName;

    private Map<String, Object> whereColumnMap = Maps.newTreeMap();

    public String getCountColumnName() {
        return countColumnName;
    }

    public QueryCountVo setCountColumnName(String countColumnName) {
        this.countColumnName = countColumnName;
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
    public <T extends TreeMap<String, Object>> QueryCountVo setWhereColumnMap(T whereColumnMap) {
        this.whereColumnMap = whereColumnMap;
        return this;
    }
}

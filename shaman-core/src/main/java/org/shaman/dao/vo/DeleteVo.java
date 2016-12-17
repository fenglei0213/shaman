package org.shaman.dao.vo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Created by fenglei on 2016/3/4.
 */
public class DeleteVo extends BaseVo {

    public DeleteVo(Class clazz) {
        super.setTableClazz(clazz);
    }

    private String primaryKeyName;

    private List idList = Lists.newArrayList();

    private Map<String, Object> whereColumnMap = Maps.newLinkedHashMap();

    public List<Long> getIdList() {
        return idList;
    }

    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    public DeleteVo setPrimaryKeyName(String primaryKeyName) {
        this.primaryKeyName = primaryKeyName;
        return this;
    }

    public DeleteVo setIdList(List<Long> idList) {
        this.idList = idList;
        return this;
    }

    public Map<String, Object> getWhereColumnMap() {
        return whereColumnMap;
    }

    /**
     * addCondition addCondition
     *
     * @param whereColumnMap
     */
    public void addCondition(Map<String, Object> whereColumnMap) {
        this.whereColumnMap = whereColumnMap;
    }

    /**
     * setWhereColumn setWhereColumn
     *
     * @param key
     * @param value
     * @return
     */
    public DeleteVo addCondition(String key, Object value) {
        this.whereColumnMap.put(key, value);
        return this;
    }
}

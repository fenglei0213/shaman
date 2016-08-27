package org.shaman.dao.vo;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Created by fenglei on 2016/8/27.
 */
public class UpdateVo<T> extends BaseVo{

    private Map<String, Object> whereColumnMap = Maps.newLinkedHashMap();

    private Map<String, List<Object>> whereColumnInMap = Maps.newLinkedHashMap();

    public UpdateVo(Class clazz){
        super.setTableClazz(clazz);
    }

    public Map<String, Object> getWhereColumnMap() {
        return whereColumnMap;
    }

    public void setWhereColumnMap(Map<String, Object> whereColumnMap) {
        this.whereColumnMap = whereColumnMap;
    }

    public Map<String, List<Object>> getWhereColumnInMap() {
        return whereColumnInMap;
    }

    public void setWhereColumnInMap(Map<String, List<Object>> whereColumnInMap) {
        this.whereColumnInMap = whereColumnInMap;
    }
}

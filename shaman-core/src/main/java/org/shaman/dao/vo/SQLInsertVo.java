package org.shaman.dao.vo;

import com.google.common.collect.Maps;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by fenglei on 2016/3/3.
 */
public class SQLInsertVo extends SQLBaseVo {

    private Map<Field, Object> sqlSetMap = Maps.newLinkedHashMap();

    public Map<Field, Object> getSqlSetMap() {
        return sqlSetMap;
    }

    public void setSqlSetMap(Map<Field, Object> sqlSetMap) {
        this.sqlSetMap = sqlSetMap;
    }
}

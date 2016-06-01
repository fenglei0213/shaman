package org.shaman.dao.vo;

import com.google.common.collect.Maps;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by fenglei on 2016/3/3.
 */
public class SQLUpdateVo extends SQLBaseVo {

    private Map<Field, Object> sqlSetMap = Maps.newLinkedHashMap();
    private Map<Field, Object> sqlWhereMap = Maps.newLinkedHashMap();

    public Map<Field, Object> getSqlSetMap() {
        return sqlSetMap;
    }

    public void setSqlSetMap(Map<Field, Object> sqlSetMap) {
        this.sqlSetMap = sqlSetMap;
    }

    public Map<Field, Object> getSqlWhereMap() {
        return sqlWhereMap;
    }

    public void setSqlWhereMap(Map<Field, Object> sqlWhereMap) {
        this.sqlWhereMap = sqlWhereMap;
    }
}

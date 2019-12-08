package org.shaman.dao.vo;

import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by fenglei on 2016/6/11.
 */
public class SQLBatchVo extends SQLBaseVo {

    private List<Map<Field, Object>> sqlSetList = Lists.newArrayList();

    private List<Map<Field,Object>> sqlWhereList = Lists.newArrayList();

    public List<Map<Field, Object>> getSqlSetList() {
        return sqlSetList;
    }

    public void setSqlSetList(List<Map<Field, Object>> sqlSetList) {
        this.sqlSetList = sqlSetList;

    }

    public List<Map<Field, Object>> getSqlWhereList() {
        return sqlWhereList;
    }

    public void setSqlWhereList(List<Map<Field, Object>> sqlWhereList) {
        this.sqlWhereList = sqlWhereList;
    }
}

package org.shaman.dao.setter;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by fenglei on 2016/6/11.
 */
public class UpdateBatchSetter extends BaseSetter implements BatchPreparedStatementSetter {

    private List<Map<Field, Object>> sqlParamList;

    public UpdateBatchSetter(List<Map<Field, Object>> sqlSetList, List<Map<Field, Object>> sqlWhereList) {
        sqlSetList.addAll(sqlWhereList);
        this.setSqlParamList(sqlSetList);
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        super.setFieldValueBatchLoop(ps, sqlParamList, i);
    }

    @Override
    public int getBatchSize() {
        return sqlParamList.size();
    }

    public void setSqlParamList(List<Map<Field, Object>> sqlParamList) {
        this.sqlParamList = sqlParamList;
    }
}

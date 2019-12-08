package org.shaman.dao.setter;

import org.shaman.dao.vo.SQLInsertVo;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * Created by fenglei on 2019/05/11.
 */
public class InsertPreparedStatementCreator extends BaseSetter implements PreparedStatementCreator {

    private Map<Field, Object> sqlSetMap;

    private String sql;

    /**
     * @param sqlInsertVo
     */
    public InsertPreparedStatementCreator(SQLInsertVo sqlInsertVo) {
        this.setSql(sqlInsertVo.getSql());
        this.setSqlSetMap(sqlInsertVo.getSqlSetMap());
    }

    /**
     * @param con
     * @return
     * @throws SQLException
     */
    @Override
    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        super.setFieldValueLoop(ps, sqlSetMap, 1);
        return ps;
    }

    public void setSqlSetMap(Map<Field, Object> sqlSetMap) {
        this.sqlSetMap = sqlSetMap;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}

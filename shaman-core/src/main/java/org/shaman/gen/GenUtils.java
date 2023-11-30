package org.shaman.gen;

import com.google.common.collect.Lists;
import org.shaman.util.HumpUtils;
import org.shaman.util.ShamanFileUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class GenUtils {

    /**
     * genJavaPojoFile 生成Java文件,主函数入口
     *
     * @param driverName
     * @param url
     * @param userName
     * @param password
     * @param tableName
     * @param outputFile
     */
    public static void genJavaPojo(String driverName,
                                   String url,
                                   String userName,
                                   String password,
                                   String tableName,
                                   String outputFile) throws IOException {
        String sql = "SELECT * FROM " + tableName + " LIMIT 1";
        SqlResVo sqlResVo = new SqlResVo();
        try {
            sqlResVo = GenUtils.connectDB(driverName, url, userName, password, sql);
            sqlResVo.setTableName(tableName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        String javaFileContent = GenUtils.genJavaPojoFile(sqlResVo);
        ShamanFileUtils.writeFile(outputFile, javaFileContent);
    }

    /**
     * connectDB 连接数据库
     *
     * @param driverName
     * @param url
     * @param uname
     * @param pwd
     * @param sql
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private static SqlResVo connectDB(
            String driverName,
            String url, String uname,
            String pwd, String sql) throws SQLException, ClassNotFoundException {
        SqlResVo sqlResVo = new SqlResVo();
        try {
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(url, uname, pwd);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int columnCount = rsMetaData.getColumnCount();
            List<String> columnNameList = Lists.newArrayList();
            List<String> columnTypeList = Lists.newArrayList();

            for (int i = 1; i <= columnCount; i++) {
                // getColumnName 拿到的是原始列名,不是别名,AS 别名的情况会重复
                String columnName = rsMetaData.getColumnName(i);
//                String columnName = rsMetaData.getColumnLabel(i);
                String columnTypeName = rsMetaData.getColumnTypeName(i);
                columnNameList.add(columnName);
                columnTypeList.add(columnTypeName);
            }
            sqlResVo.setColumnNameList(columnNameList);
            sqlResVo.setColumnTypeList(columnTypeList);
        } catch (SQLException | ClassNotFoundException e) {
            throw e;
        }
        return sqlResVo;
    }

    /**
     * genJavaPojoFile 生成 Java 文件
     *
     * @param sqlResVo
     */
    private static String genJavaPojoFile(SqlResVo sqlResVo) {
        StringBuilder sb = new StringBuilder();
        String tableName = sqlResVo.getTableName();
        String className = HumpUtils.capitalizeFirstLetter(HumpUtils.camelName(tableName));
        sb.append("public class ").append(className).append(" {");
        // @FieldMeta(id = true)
        // private Long id;
        List<String> columnNameList = sqlResVo.getColumnNameList();
        List<String> columnTypeList = sqlResVo.getColumnTypeList();
        for (int i = 0; i < columnNameList.size(); i++) {
            if ("id".equals(columnNameList.get(i))) {
                sb.append("@FieldMeta(id = true)");
            } else {
                sb.append("@FieldMeta");
            }
            sb.append("private ").append(columnTypeList.get(i))
                    .append(" ").append(columnNameList.get(i)).append(";");
        }
        sb.append("}");
        return sb.toString();
    }
}

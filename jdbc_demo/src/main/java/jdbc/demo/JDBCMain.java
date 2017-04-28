package jdbc.demo;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * jdbc 步骤
 （1）  加载JDBC驱动
 （2）  建立并获取数据库连接
 （3）  创建 JDBC Statements 对象
 （4）  设置SQL语句的传入参数
 （5）  执行SQL语句并获得查询结果
 （6）  对查询结果进行转换处理并将处理结果返回
 （7）  释放相关资源（关闭Connection，关闭Statement，关闭ResultSet）
 *
 * Created by piguanghua on 2017/2/5.
 */
public class JDBCMain {
    public static void main(String args[]) {
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();

        try {
            //加载JDBC驱动
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            String url = "jdbc:oracle:thin:@localhost:1521:ORACLEDB";

            String user = "trainer";
            String password = "trainer";

            //获取数据库连接
            connection = DriverManager.getConnection(url,user,password);

            String sql = "select * from userinfo where user_id = ? ";
            //创建Statement对象（每一个Statement为一次数据库执行请求）
            stmt = connection.prepareStatement(sql);

            //设置传入参数
            stmt.setString(1, "zhangsan");

            //执行SQL语句
            rs = stmt.executeQuery();

            //处理查询结果（将查询结果转换成List<Map>格式）
            ResultSetMetaData rsmd = rs.getMetaData();
            int num = rsmd.getColumnCount();

            while(rs.next()){
                Map map = new HashMap();
                for(int i = 0;i < num;i++){
                    String columnName = rsmd.getColumnName(i+1);
                    map.put(columnName,rs.getString(columnName));
                }
                resultList.add(map);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭结果集
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                //关闭执行
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

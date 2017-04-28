package com.study.mybatis.free.page.two.interceptor;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by piguanghua on 2017/2/27.
 * 减少code污染的一种思路
 * 正则匹配sql语句，如果发现sql语句是以select开头的，则在sql语句的末尾加上分页的语句
 * 缺点：这样做对于子查询返回作为另外一sql的查询对象,会有问题。个人感觉
 * 该插件只研究mysql
 *
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class PageInterceptor implements Interceptor {

    /**
     * 分页插件思路：
     * ①获取执行语句的sql
     * ②匹配
     * ③匹配成功，进行拦截。获取查询总数，添加limit语句
     *
     *  重要的Code进行展示
     private Statement prepareStatement(StatementHandler handler, Log statementLog) throws SQLException {
     Statement stmt;
     Connection connection = getConnection(statementLog);
     stmt = handler.prepare(connection, transaction.getTimeout());
     handler.parameterize(stmt);
     return stmt;
     }

     The  object  used  for  executing  a  static  SQL  statement  and  returning  the  results  it  produces.
     By  default,  only  one  ResultSet  object  per  Statement  object  can  be  open  at  the  same  time.  Therefore,  if  the  reading  of  one  ResultSet  object  is  interleaved  with  the  reading  of  another,  each  must  have  been  generated  by  different  Statement  objects.  All  execution  methods  in  the  Statement  interface  implicitly  close  a  statment's  current  ResultSet  object  if  an  open  one  exists.

     https://coding.net/u/medusa/p/mybatis-page/git/blob/master/src/main/java/org/yxs/plugin/page/interceptor/PageUtil.java
     http://www.cnblogs.com/zhengyun_ustc/p/slowquery3.html  mysql
     http://wiki.jikexueyuan.com/project/mybatis-in-action/mybatis-paging.html 网上视频

     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
       //确认是不是Select语句
        if(!mappedStatement.getSqlCommandType().equals(SqlCommandType.SELECT)){
            return invocation.proceed();
        }
        //当前需要执行的SQL
        String sql = (String) metaObject.getValue("delegate.boundSql.sql");
        String countSql = this.getTotalSQL(sql);
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        //获取拦截方法参数，根据插件签名，知道是Connection对象.
        Connection connection = (Connection) invocation.getArgs()[0];
       int total = this.getTotal(mappedStatement, connection, countSql, boundSql);
      /*  PreparedStatement stmt = connection.prepareStatement(countSql);
        int total = 0;
        try {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                total = resultSet.getInt("total");
            }
        }finally {

        }*/
        int pageSize = 5;
        int totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        System.out.println("total :" + total + "    totalPage :" + totalPage );

        String pageSql = this.generatePageSql(sql, 0, 4);


  //      BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        metaObject.setValue("delegate.boundSql", pageSql);
        //if(id)
        return invocation.proceed();
    }

    int getTotal( MappedStatement statement, Connection connection, String  countSql,  BoundSql boundSql) throws SQLException {


        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(countSql);
            BoundSql totalSql = new BoundSql(statement.getConfiguration(), countSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
            ParameterHandler handle = new DefaultParameterHandler(statement, boundSql.getParameterObject(), totalSql);
            handle.setParameters(ps);
            ResultSet resultSet = ps.executeQuery();
            boolean resultSetResult = resultSet.next();
            while (resultSetResult) {
                System.out.println("result");
                return resultSet.getInt("total");
            }
        } finally {
            ps.close();
        }
        return 0;
    }

    private String generatePageSql(String sql, int pageNum, int pageSize){
        StringBuffer pageSql = new StringBuffer();
        pageSql.append(sql);
        pageSql.append(" limit " + pageNum + ","
                + pageSize);
        return  pageSql.toString();
    }

    private String getTotalSQL(String currSql) {
        return "select count(*) as total from (" + currSql +") as  temp";
    }


    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private boolean checkSelect(String sql) {
        String trimSql = sql.trim();
        int idx = trimSql.toLowerCase().indexOf("select");
        return idx == 0;
    }
}

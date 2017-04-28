package com.study.mybatis.free.page.interceptor;

import com.study.mybatis.free.page.domain.Pagination;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Properties;
import java.sql.Connection;

/**
 * Created by piguanghua on 2017/2/23.
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
//@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class PageInterceptor implements Interceptor {


    /* (non-Javadoc)
     * 拦截器要执行的方法
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        String id = mappedStatement.getId();
        if (id.matches(".+ByPage$")) {

            BoundSql boundSql = statementHandler.getBoundSql();
            Map<String, Object> params = (Map<String, Object>) boundSql.getParameterObject();
            Pagination page = (Pagination) params.get("page");
            String sql = boundSql.getSql();
        //    String countSql = "select count(*)from (" + sql + ")";
            String countSql = "select count(*)from  user";
            Connection connection = (Connection) invocation.getArgs()[0];
            PreparedStatement countStatement = connection.prepareStatement(countSql);
            ParameterHandler parameterHandler = (ParameterHandler) metaObject.getValue("delegate.parameterHandler");
            parameterHandler.setParameters(countStatement);
            ResultSet rs = countStatement.executeQuery();
            if (rs.next()) {
                page.setTotalNumber(rs.getInt(1));
            }
            String pageSql = sql + " limit " + page.getStartIndex() + "," + page.getTotalSelect();
            metaObject.setValue("delegate.boundSql.sql", pageSql);
        }
        return invocation.proceed();
    }

    /* (non-Javadoc)
    * 拦截器需要拦截的对象
    */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /* (non-Javadoc)
    * 设置初始化的属性值
    */
    @Override
    public void setProperties(Properties props) {

    }
}

package com.study.mybatis.page.interceptor.mybatis_book;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Created by piguanghua on 2017/2/26.
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class PageInterceptorVersionTwo implements Interceptor {

    private final static String SQL_SELECT_REGEX = "(?is)^\\s*SELECT.*$";

    private final static String SQL_COUNT_REGEX = "(?is)^\\s*SELECT\\s+COUNT\\s*\\(\\s*(?:\\*|\\w+)\\s*\\).*$";

    @Override
    public Object intercept(Invocation inv) throws Throwable {
        StatementHandler target = (StatementHandler) inv.getTarget();
        BoundSql boundSql = target.getBoundSql();
        String sql = boundSql.getSql();
        if (StringUtils.isBlank(sql)) {
            return inv.proceed();
        }
        // 只有为select查询语句时才进行下一步
        /*if (sql.matches(SQL_SELECT_REGEX)
                && !Pattern.matches(SQL_COUNT_REGEX, sql))*/
        if(sql.matches(SQL_SELECT_REGEX)){
            Object obj = FieldUtils.readField(target, "delegate", true);
            // 反射获取 RowBounds 对象。
            RowBounds rowBounds = (RowBounds) FieldUtils.readField(obj,
                    "rowBounds", true);
            // 分页参数存在且不为默认值时进行分页SQL构造
            //if (rowBounds != null && rowBounds != RowBounds.DEFAULT)
            if (rowBounds != null ) {
                FieldUtils.writeField(boundSql, "sql", newSql(sql, rowBounds),
                        true);
                System.out.println("new sql>>>>>"
                        + boundSql.getSql().replaceAll("\n", ""));
                // 一定要还原否则将无法得到下一组数据(第一次的数据被缓存了)
                FieldUtils.writeField(rowBounds, "offset",
                        RowBounds.NO_ROW_OFFSET, true);
                FieldUtils.writeField(rowBounds, "limit",
                        RowBounds.NO_ROW_LIMIT, true);
            }
        }
        return inv.proceed();
    }

    public String newSql(String oldSql, RowBounds rowBounds) {
        String end = " limit " + rowBounds.getOffset() + "," + rowBounds.getLimit();
        return oldSql + end;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}

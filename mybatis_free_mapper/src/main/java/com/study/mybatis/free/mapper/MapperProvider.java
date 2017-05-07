package com.study.mybatis.free.mapper;

/**
 * Created by piguanghua on 2017/5/7.
 */
public class MapperProvider {
    public static final String DYNAMIC_SQL = "dynamicSQL";
    public String dynamicSQL(Object record) {
        return DYNAMIC_SQL;
    }
}

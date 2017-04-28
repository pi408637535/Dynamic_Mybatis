package com.study.general.mapper.mapper;

import com.study.general.mapper.helper.MapperHelper;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

/**
 * Created by piguanghua on 2017/4/4.
 */
public interface Mapper<T> {
    @SelectProvider(type = MapperHelper.class, method = MapperHelper.DYNAMIC_SQL)
    List<T> select(T record);

    @InsertProvider(type = MapperHelper.class, method = MapperHelper.DYNAMIC_SQL)
    int insert(T record);

    @DeleteProvider(type = MapperHelper.class, method = MapperHelper.DYNAMIC_SQL)
    int delete(T key);

    @UpdateProvider(type = MapperHelper.class, method = MapperHelper.DYNAMIC_SQL)
    int updateByPrimaryKey(T record);
}

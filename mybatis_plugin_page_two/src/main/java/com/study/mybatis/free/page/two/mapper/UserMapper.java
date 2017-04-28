package com.study.mybatis.free.page.two.mapper;


import com.study.mybatis.free.page.two.domain.User;
import com.study.mybatis.free.page.two.domain.UserExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    long countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<User> selectUser(RowBounds rowBounds);
    List<User> selectUserByMap(Map<String, Object> map);
    List<User> selectByPage(Map<String, Object> map);
    List<User> selectUserByPaginPluginMap(Map<String, Object> params);
}
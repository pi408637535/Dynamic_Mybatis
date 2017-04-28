package com.it.page.mapper;

import com.it.page.domain.User;
import com.it.page.domain.UserExample;
import java.util.List;

import com.it.page.model.Page;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    User findUserById(int id) throws Exception;

    List<User> findPag(Page page);
}
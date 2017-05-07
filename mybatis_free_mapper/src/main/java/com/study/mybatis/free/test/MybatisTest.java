package com.study.mybatis.free.test;

import com.study.mybatis.free.domain.User;
import com.study.mybatis.free.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by piguanghua on 2017/5/7.
 */
public class MybatisTest {
    public static void main(String args[]) throws IOException {

        Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession session = sqlSessionFactory.openSession();
//        session.getConfiguration().addMapper(UserMapper.class);
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User user = userMapper.selectByPrimaryKey(1L);
        System.out.println("Current details of the user are "+user.toString());
    }
}

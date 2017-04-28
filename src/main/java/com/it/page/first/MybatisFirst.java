package com.it.page.first;


import com.it.page.domain.User;
import com.it.page.mapper.UserMapper;
import com.it.page.model.Page;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by piguanghua on 2017/1/31.
 */
public class MybatisFirst {
    // 根据id查询用户信息，得到一条记录结果
    @Test
    public void findUserByIdTest() throws IOException {

        // mybatis配置文件
        String resource = "SqlMapConfig.xml";
        // 得到配置文件流
        InputStream inputStream = Resources.getResourceAsStream(resource);

        // 创建会话工厂，传入mybatis的配置文件信息
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder()
                .build(inputStream);

        // 通过工厂得到SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();

        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        Page<User> page = new Page<User>();
        page.setPageNo(2);
        List<User> users = userMapper.findPag(page);
        page.setResults(users);
        System.out.println(page);

        // 释放资源
        sqlSession.close();

    }
}

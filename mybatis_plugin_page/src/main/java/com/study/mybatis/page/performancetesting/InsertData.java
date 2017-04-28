package com.study.mybatis.page.performancetesting;

import com.study.mybatis.page.domain.PageParams;
import com.study.mybatis.page.domain.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by piguanghua on 2017/2/26.
 */
public class InsertData {
    @Test
    public void testPagingPlugin() throws IOException {
        // mybatis配置文件
        String resource = "SqlMapConfig.xml";
        // 得到配置文件流
        InputStream inputStream = Resources.getResourceAsStream(resource);

        // 创建会话工厂，传入mybatis的配置文件信息
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder()
                .build(inputStream);

        // 通过工厂得到SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();

        for (int i = 0; i < 1000000; i++) {
            if(i % 1000 == 0){
                sqlSession.commit();
            }
            String string = RandomStringUtils.randomAlphanumeric(5);
      //      sqlSession.insert("insertSelective", new User(string));
        }

        sqlSession.close();
    }
}

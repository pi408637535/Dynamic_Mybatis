package com.study.mybatis.page.performancetesting;

import com.study.mybatis.page.domain.User;
import com.study.mybatis.page.interceptor.pagehelper.Page;
import com.study.mybatis.page.interceptor.pagehelper.PageHelper;
import com.study.mybatis.page.mapper.UserMapper;
import org.apache.commons.lang3.RandomUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by piguanghua on 2017/2/26.
 */
public class TestPageHelper {

    @Test
    public  void testPageHelper() throws IOException {
        // mybatis配置文件
        String resource = "SqlMapConfig.xml";
        // 得到配置文件流
        InputStream inputStream = Resources.getResourceAsStream(resource);

        // 创建会话工厂，传入mybatis的配置文件信息
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder()
                .build(inputStream);
        // 通过工厂得到SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();

        long starTime=System.currentTimeMillis();

        for(int i = 0; i < 1000; i++){
            int page = RandomUtils.nextInt(1,100000);
            int size = RandomUtils.nextInt(1,1000);
            PageHelper.startPage(page, size, false);
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            //不进行count查询时，返回结果就是List<sysLoginLogMapperSysLoginLog>类型
            List<User> pageResult = userMapper.selectUserByPageHelper();
            System.out.println("i=:" + i + "  " + pageResult.size());
        }

        long endTime=System.currentTimeMillis();
        long Time=endTime-starTime;
        System.out.println(Time);
    }
}

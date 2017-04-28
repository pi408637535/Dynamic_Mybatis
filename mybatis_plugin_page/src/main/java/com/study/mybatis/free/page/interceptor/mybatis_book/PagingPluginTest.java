package com.study.mybatis.free.page.interceptor.mybatis_book;

import com.study.mybatis.free.page.domain.PageParams;
import com.study.mybatis.free.page.domain.User;
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

/**
 * Created by piguanghua on 2017/2/25.
 */
public class PagingPluginTest {

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

        Map<String, Object> paramMap = new HashMap<String, Object>();
        PageParams pageParams = new PageParams();
        pageParams.setUseFlag(true);
        pageParams.setCheckFlag(false);
        pageParams.setPage(2);
        pageParams.setPageSize(5);
        paramMap.put("page_drsdsd2233", pageParams);
        List<User> userList = sqlSession.selectList("selectUserByPaginPluginMap", pageParams);
        System.out.println(userList.size());
    }
}

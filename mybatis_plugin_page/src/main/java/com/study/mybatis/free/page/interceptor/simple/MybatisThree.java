package com.study.mybatis.free.page.interceptor.simple;

import com.study.mybatis.free.page.domain.Pagination;
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
 * Created by piguanghua on 2017/2/23.
 */
public class MybatisThree {
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

        // 通过SqlSession操作数据库
        // 第一个参数：映射文件中statement的id，等于=namespace+"."+statement的id
        // 第二个参数：指定和映射文件中所匹配的parameterType类型的参数
        // sqlSession.selectOne结果 是与映射文件中所匹配的resultType类型的对象
        // selectOne查询出一条记录'
        Map<String,Object> map =new HashMap<>();

        Pagination pagination = new Pagination();
        pagination.setStartIndex(3);
        pagination.setTotalSelect(4);

        map.put("page", pagination);

        List<User> userList = sqlSession.selectList("selectByPage", map);

        System.out.println(userList.size());

        // 释放资源
        sqlSession.close();
    }

}

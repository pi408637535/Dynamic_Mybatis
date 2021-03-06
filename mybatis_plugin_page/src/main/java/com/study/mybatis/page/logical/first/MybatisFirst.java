package com.study.mybatis.page.logical.first;


import com.study.mybatis.page.domain.User;
import com.study.mybatis.page.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.RowBounds;
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

        // 通过SqlSession操作数据库
        // 第一个参数：映射文件中statement的id，等于=namespace+"."+statement的id
        // 第二个参数：指定和映射文件中所匹配的parameterType类型的参数
        // sqlSession.selectOne结果 是与映射文件中所匹配的resultType类型的对象
        // selectOne查询出一条记录
        User user = sqlSession.selectOne("selectByPrimaryKey", 1L);

        System.out.println(user);

        // 释放资源
        sqlSession.close();
    }

    @Test
    public void testSelect() {
        try {
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
            int start = 3;//正确的叫法应该是offset
            int pagesize = 3;//<span style="font-family: 'Microsoft YaHei';">正确的叫法应该是limit</span>
            RowBounds rowBounds = new RowBounds(start,pagesize);
            List<User> users= userMapper.selectUser(rowBounds);
            for(User ui:users){
                System.out.println(ui);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

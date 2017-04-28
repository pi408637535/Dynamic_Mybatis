package com.study.mybatis.free.page.interceptor.pagehelper;

import com.study.mybatis.free.page.domain.User;
import com.study.mybatis.free.page.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * Created by piguanghua on 2017/2/26.
 */
public class TestPageHelperByStartPage {
    @Test
    public void testPageHelperByStartPage() throws Exception {
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

        String logip = "";
        String username = "super";
        String loginDate = "";
        String exitDate = null;
        String logerr = null;
        //不进行count查询，第三个参数设为false
        PageHelper.startPage(1, 10, false);
        //不进行count查询时，返回结果就是List<sysLoginLogMapperSysLoginLog>类型
        List<User> userList = userMapper.selectUserByPageHelper();
        System.out.println(userList.size());

      /*  //当第三个参数没有或者为true的时候，进行count查询
        PageHelper.startPage(2, 10);
        //返回结果默认是List<SysLoginLog>
        //可以通过强制转换为Page<SysLoginLog>,该对象除了包含返回结果外，还包含了分页信息
        Page<SysLoginLog> page = (Page<SysLoginLog>) sysLoginLogMapper
                .findSysLoginLog(logip, username, loginDate, exitDate, logerr);
        Assert.assertEquals(10, page.getResult().size());
        //进行count查询，返回结果total>0
        Assert.assertTrue(page.getTotal() > 0);*/
    }
}

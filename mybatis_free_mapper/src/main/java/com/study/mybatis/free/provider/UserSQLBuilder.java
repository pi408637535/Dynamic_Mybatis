package com.study.mybatis.free.provider;

import com.study.mybatis.free.domain.User;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

/**
 * Created by piguanghua on 2017/3/11.
 * @SelectProvide中type参数指定的Class类，必须要能够通过无参的构造函数来初始化。
 * @SelectProvide中method参数指定的方法，必须是public的，返回值必须为String，可以为static。
 *
 */
public class UserSQLBuilder {

    public String getUsersProvider(Map parameters) {

        String id = (String) parameters.get("id");
        String status = (String) parameters.get("status");

        StringBuilder query = new StringBuilder();
        query.append("select id, user_name, user_address, status from users");
        if (status.equals("NA") && !id.equals("NA")) {
            query.append(" where id like '%" + id + "%'");

        } else if (!status.equals("NA") && id.equals("NA")) {
            query.append(" where status ='" + status + "'");
        }
        return query.toString();
    }

    public String insertUser(final User user){
        return new SQL()
        {
            {
                INSERT_INTO("user");
                if (user.getName() != null)
                {
                    VALUES("name", "#{name}");
                }

            }
        }.toString();
    }
}

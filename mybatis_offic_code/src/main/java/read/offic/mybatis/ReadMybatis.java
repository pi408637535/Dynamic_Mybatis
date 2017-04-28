package read.offic.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

/**
 * Created by piguanghua on 2017/2/6.
 */
public class ReadMybatis {
    public static void main(String args[]) throws Exception {
        /**
         *  获取主配置文件 数据流
         */
        String resource = "org/mybatis/example/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);

        /**

         SqlSessionFactoryBuilder

         public SqlSessionFactory build(InputStream inputStream) {
            return this.build((InputStream)inputStream, (String)null, (Properties)null);
         }

    public SqlSessionFactory build(InputStream inputStream, String environment, Properties properties) {
            SqlSessionFactory var5;
            try {
                XMLConfigBuilder e = new XMLConfigBuilder(inputStream, environment, properties);
                var5 = this.build(e.parse());
            } catch (Exception var14) {
                throw ExceptionFactory.wrapException("Error building SqlSession.", var14);
            } finally {
                ErrorContext.instance().reset();

                try {
                    inputStream.close();
                } catch (IOException var13) {
                    ;
                }
            }
            return var5;
        }




         */

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();



    }


}
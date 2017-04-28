package com.pradeep.mybatis.test;

import com.pradeep.mybatis.mapper.StudentMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

public class AnnotationDeleteProviderTest {
	public static void main(String args[]) throws IOException {

		Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml");
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder()
				.build(reader);
		SqlSession session = sqlSessionFactory.openSession();
		session.getConfiguration().addMapper(StudentMapper.class);

		StudentMapper mapper = session.getMapper(StudentMapper.class);
		mapper.deleteStudent(15);
		System.out.println("record deleted successfully");
		session.commit();
		session.close();

	}
}

package com.pradeep.mybatis.test;

import com.pradeep.mybatis.mapper.StudentMapper;
import com.pradeep.mybatis.model.Student;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

public class AnnotationInsertProviderTest {

	public static void main(String args[]) throws IOException {

		Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml");
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder()
				.build(reader);
		SqlSession session = sqlSessionFactory.openSession();
		session.getConfiguration().addMapper(StudentMapper.class);

		StudentMapper mapper = session.getMapper(StudentMapper.class);

		Student student = new Student();
		student.setName("Pradeep");
		student.setBranch("EEE");
		student.setEmail("pradeepbhat4591@gmail.com");
		student.setPercentage(90);
		student.setPhone(123412341);

		mapper.insertStudent(student);
		System.out.println("record inserted successfully");
		session.commit();
		session.close();

	}
}

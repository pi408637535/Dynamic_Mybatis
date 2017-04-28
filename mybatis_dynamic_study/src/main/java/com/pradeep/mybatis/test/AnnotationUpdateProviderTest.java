package com.pradeep.mybatis.test;

import com.pradeep.mybatis.mapper.StudentMapper;
import com.pradeep.mybatis.model.Student;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

public class AnnotationUpdateProviderTest {

	public static void main(String args[]) throws IOException{
	      
		   Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml");
		   SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);		
		   SqlSession session = sqlSessionFactory.openSession();   
		   session.getConfiguration().addMapper(StudentMapper.class);
		   StudentMapper mapper = session.getMapper(StudentMapper.class);
		      
		   Student student = mapper.findStudentById(15);
		   System.out.println("Current details of the student are "+student.toString());  
		      
		   student.setEmail("Shyam123@yahoo.com");
		   student.setPhone(984802233);
		      
		   
		   mapper.updateStudent(student);
		   System.out.println("Record updated successfully");   
		   session.commit();   
		   session.close();

		}	
}

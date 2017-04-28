package com.study.ibatis;

import com.study.ibatis.dao.impl.StudentDaoImpl;
import com.study.ibatis.entity.Student;

import java.sql.Date;
import java.util.List;

/**
 * Created by piguanghua on 2017/2/17.
 */
public class TestIbatis {
    public static void main(String[] args) {
        StudentDaoImpl studentDaoImpl = new StudentDaoImpl();

        System.out.println("测试插入");
        Student addStudent = new Student();
        addStudent.setName("李四");
        addStudent.setBirth(Date.valueOf("2011-09-02"));
        addStudent.setScore(88);
        System.out.println(studentDaoImpl.addStudent(addStudent));

        System.out.println("测试根据id查询");
        System.out.println(studentDaoImpl.selectStudentById(1));

        System.out.println("测试模糊查询");
        List<Student> mohuLists = studentDaoImpl.selectStudentByName("李");
        for (Student student : mohuLists) {
            System.out.println(student);
        }

        System.out.println("测试查询所有");
        List<Student> students = studentDaoImpl.selectAllStudent();
        for (Student student : students) {
            System.out.println(student);
        }

        System.out.println("根据id删除学生信息");
        System.out.println(studentDaoImpl.deleteStudentById(1));

        System.out.println("测试更新学生信息");
        Student updateStudent = new Student();
        updateStudent.setId(1);
        updateStudent.setName("李四1");
        updateStudent.setBirth(Date.valueOf("2011-08-07"));
        updateStudent.setScore(21);
        System.out.println(studentDaoImpl.updateStudent(updateStudent));

    }
}

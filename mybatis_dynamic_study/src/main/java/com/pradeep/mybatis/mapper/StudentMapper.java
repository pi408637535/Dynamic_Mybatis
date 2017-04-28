package com.pradeep.mybatis.mapper;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.pradeep.mybatis.data.StudentDynamicSqlProvider;
import com.pradeep.mybatis.model.Student;

public interface StudentMapper {

    @SelectProvider(type = StudentDynamicSqlProvider.class, method = "findStudentByIdSql")
    Student findStudentById(Integer id);

    @InsertProvider(type = StudentDynamicSqlProvider.class, method = "insertStudent")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertStudent(Student student);

    @UpdateProvider(type = StudentDynamicSqlProvider.class, method = "updateStudent")
    int updateStudent(Student student);

    @DeleteProvider(type = StudentDynamicSqlProvider.class, method = "deleteStudent")
    int deleteStudent(int studentId);

    @SelectProvider(type = StudentDynamicSqlProvider.class, method = "findStudentByNameAndEmail")
    Student findStudentByNameAndEmail(@Param("name")String name,@Param("email") String email);

}

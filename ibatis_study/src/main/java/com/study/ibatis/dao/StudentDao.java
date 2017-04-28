package com.study.ibatis.dao;

import com.study.ibatis.entity.Student;

import java.util.List;

/**
 * Created by piguanghua on 2017/2/17.
 */
public interface StudentDao {
    /**
     * 添加学生信息
     *
     * @param student
     *            学生实体
     * @return 返回是否添加成功
     */
    public boolean addStudent(Student student);

    /**
     * 根据学生id删除学生信息
     *
     * @param id
     *            学生id
     * @return 删除是否成功
     */
    public boolean deleteStudentById(int id);

    /**
     * 更新学生信息
     *
     * @param student
     *            学生实体
     * @return 更新是否成功
     */
    public boolean updateStudent(Student student);

    /**
     * 查询全部学生信息
     *
     * @return 返回学生列表
     */
    public List<Student> selectAllStudent();

    /**
     * 根据学生姓名模糊查询学生信息
     *
     * @param name
     *            学生姓名
     * @return 学生信息列表
     */
    public List<Student> selectStudentByName(String name);

    /**
     * 根据学生id查询学生信息
     *
     * @param id
     *            学生id
     * @return 学生对象
     */
    public Student selectStudentById(int id);
}

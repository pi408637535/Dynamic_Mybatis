package com.study.mybatis.page.study.metaobject;

/**
 * Created by piguanghua on 2017/3/5.
 */
public class Student {
    int age = 0;
    String name = "tian";

    protected String job;

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

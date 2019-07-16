package com.spring.service.impl;

import com.spring.service.StudentService;

import java.util.Date;

/**
 * StudentServiceImpl class
 *
 * @author BowenWang
 * @date 2019/07/15
 */
public class StudentServiceImpl2 implements StudentService {

    private String name;
    private Integer age;
    private Date birthday;

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void saveStudent() {
        System.out.println("保存学生。。。" + toString());
    }

    @Override
    public String toString() {
        return "StudentServiceImpl{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", birthday=" + birthday +
                '}';
    }
}

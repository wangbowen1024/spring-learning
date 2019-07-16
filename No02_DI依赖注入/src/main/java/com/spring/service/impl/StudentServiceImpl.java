package com.spring.service.impl;

import com.spring.service.StudentService;

import java.util.Date;

/**
 * StudentServiceImpl class
 *
 * @author BowenWang
 * @date 2019/07/15
 */
public class StudentServiceImpl implements StudentService {

    private String name;
    private Integer age;
    private Date birthday;

    public StudentServiceImpl(String name, Integer age, Date birthday) {
        this.name = name;
        this.age = age;
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

package com.spring.ui;

import com.spring.service.StudentService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Client class
 *
 * @author BowenWang
 * @date 2019/07/15
 */
public class Client {

    public static void main(String[] args) {
        // 1.获取核心容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean.xml");
        // 2.根据ID获取bean对象
        StudentService studentService = (StudentService) applicationContext.getBean("studentService3");
        studentService.saveStudent();


    }
}

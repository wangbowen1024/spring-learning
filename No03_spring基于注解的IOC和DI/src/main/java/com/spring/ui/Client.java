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
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean.xml");
        // 2.根据ID获取bean对象(第二个参数class是因为spring容器是一个Map其中key是String，value是Object)
        StudentService studentService = applicationContext.getBean("studentService", StudentService.class);
        studentService.saveStudent();
        applicationContext.close();
    }
}

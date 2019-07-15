package com.spring.factory;

import com.spring.service.StudentService;
import com.spring.service.impl.StudentServiceImpl;

/**
 * InstanceFactory class
 *
 * 模拟一个工厂类
 *
 * @author BowenWang
 * @date 2019/07/15
 */
public class InstanceFactory {

    public StudentService getStudentService() {
        return new StudentServiceImpl();
    }
}

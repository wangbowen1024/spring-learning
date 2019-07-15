package com.spring.factory;

import com.spring.service.StudentService;
import com.spring.service.impl.StudentServiceImpl;

/**
 * StaticFactory class
 *
 * 模拟静态工厂方法
 *
 * @author BowenWang
 * @date 2019/07/15
 */
public class StaticFactory {

    public static StudentService getStudentService() {
        return new StudentServiceImpl();
    }
}

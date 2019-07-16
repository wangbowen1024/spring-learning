package com.spring.dao.impl;

import com.spring.dao.StudentDao;
import org.springframework.stereotype.Repository;

/**
 * StudentDaoImpl class
 *
 * @author BowenWang
 * @date 2019/07/16
 */
@Repository
public class StudentDaoImpl implements StudentDao {
    public void saveStudent() {
        System.out.println("保存学生到数据库。。。");
    }
}

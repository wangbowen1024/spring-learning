package com.spring.test;

import com.spring.dao.AccountDao;
import com.spring.domain.Account;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * JdbcTemplateTest1 class
 *
 * @author BowenWang
 * @date 2019/07/26
 */
public class JdbcTemplateTest1 {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean.xml");
        AccountDao accountDao = applicationContext.getBean("accountDao", AccountDao.class);
        Account account = accountDao.findById(1);
        System.out.println(account);
    }
}

package com.spring.dao;

import com.spring.domain.Account;

/**
 * AccountDao class
 *
 * @author BowenWang
 * @date 2019/07/28
 */
public interface AccountDao {

    /**
     * 根据姓名查找账户
     * @param name
     * @return
     */
    Account findAccountByName(String name);

    /**
     * 更新账户
     * @param account
     */
    void updateAccount(Account account);



}

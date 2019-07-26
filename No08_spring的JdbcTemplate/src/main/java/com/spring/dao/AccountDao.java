package com.spring.dao;

import com.spring.domain.Account;

/**
 * AccountDao class
 *
 * @author BowenWang
 * @date 2019/07/26
 */
public interface AccountDao {
    /**
     * 根据ID查找账户
     * @param id
     */
    Account findById(int id);

    /**
     * 根据名字查找唯一账户
     * @param name
     * @return
     */
    Account findByName(String name);

    /**
     * 更新账户
     * @param account
     */
    void updateAccount(Account account);

    /**
     * 统计总账户数量
     * @return
     */
    long totalAccount();
}

package com.spring.service;

/**
 * AccountService class
 *
 * @author BowenWang
 * @date 2019/07/25
 */
public interface AccountService {
    /**
     * 保存账户
     */
    void saveAccount();

    /**
     * 更新账户
     * @param i
     */
    void updateAccount(int i);

    /**
     * 删除账户
     * @return
     */
    int deleteAccount();
}

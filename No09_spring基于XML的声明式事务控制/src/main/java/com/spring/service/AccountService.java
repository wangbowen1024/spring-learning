package com.spring.service;

import com.spring.domain.Account;

/**
 * AccountService class
 *
 * @author BowenWang
 * @date 2019/07/28
 */
public interface AccountService {

    /**
     * 根据Name查找账户
     * @param name
     * @return
     */
    Account findAccountByName(String name);

    /**
     * 转账
     * @param outAccount    转出账户
     * @param inAccount     转入账户
     * @param money         金额
     */
    void transfer(String outAccount, String inAccount, float money);
}

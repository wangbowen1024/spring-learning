package com.spring.service.impl;

import com.spring.service.AccountService;

/**
 * AccountServiceImpl class
 *
 * @author BowenWang
 * @date 2019/07/25
 */
public class AccountServiceImpl implements AccountService {
    public void saveAccount() {
        System.out.println("保存账户。。。");
        //int i = 1/0;
    }

    public void updateAccount(int i) {
        System.out.println("更新账户。。。" + i);
    }

    public int deleteAccount() {
        System.out.println("删除账户。。。");
        return 0;
    }
}

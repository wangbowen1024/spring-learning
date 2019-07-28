package com.spring.service.impl;

import com.spring.dao.AccountDao;
import com.spring.domain.Account;
import com.spring.service.AccountService;

/**
 * AccountServiceImpl class
 *
 * @author BowenWang
 * @date 2019/07/28
 */
public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao;

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public Account findAccountByName(String name) {
        return accountDao.findAccountByName(name);
    }

    @Override
    public void transfer(String outAccount, String inAccount, float money) {
        Account out = accountDao.findAccountByName(outAccount);
        Account in = accountDao.findAccountByName(inAccount);
        out.setMoney(out.getMoney() - money);
        in.setMoney(in.getMoney() + money);
        accountDao.updateAccount(out);
        int error = 1 / 0;
        accountDao.updateAccount(in);
    }
}

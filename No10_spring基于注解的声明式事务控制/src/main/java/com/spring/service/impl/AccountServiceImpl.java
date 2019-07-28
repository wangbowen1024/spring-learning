package com.spring.service.impl;

import com.spring.dao.AccountDao;
import com.spring.domain.Account;
import com.spring.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * AccountServiceImpl class
 *
 * @author BowenWang
 * @date 2019/07/28
 */
@Service("accountService")
@Transactional(propagation= Propagation.SUPPORTS, readOnly=true)//只读型事务的配置
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Override
    public Account findAccountByName(String name) {
        return accountDao.findAccountByName(name);
    }

    @Override
    //需要的是读写型事务配置
    @Transactional(propagation = Propagation.REQUIRED)
    public void transfer(String outAccount, String inAccount, float money) {
        Account out = accountDao.findAccountByName(outAccount);
        Account in = accountDao.findAccountByName(inAccount);
        out.setMoney(out.getMoney() - money);
        in.setMoney(in.getMoney() + money);
        accountDao.updateAccount(out);
        //int error = 1 / 0;
        accountDao.updateAccount(in);
    }
}

package com.spring.dao.impl;

import com.spring.dao.AccountDao;
import com.spring.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * AccountDaoImpl class
 *
 * @author BowenWang
 * @date 2019/07/28
 */
@Repository("accountDao")
public class AccountDaoImpl implements AccountDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Account findAccountByName(String name) {
        List<Account> accounts = jdbcTemplate.query("select * from account where name = ?",
                new BeanPropertyRowMapper<Account>(Account.class), name);
        if (accounts != null && accounts.size() == 1) {
            return accounts.get(0);
        }
        return null;
    }

    @Override
    public void updateAccount(Account account) {
        jdbcTemplate.update("update account set money = ? where id = ?", account.getMoney(), account.getId());
    }
}

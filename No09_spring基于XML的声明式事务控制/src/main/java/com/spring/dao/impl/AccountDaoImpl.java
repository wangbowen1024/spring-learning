package com.spring.dao.impl;

import com.spring.dao.AccountDao;
import com.spring.domain.Account;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * AccountDaoImpl class
 *
 * @author BowenWang
 * @date 2019/07/28
 */
public class AccountDaoImpl implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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

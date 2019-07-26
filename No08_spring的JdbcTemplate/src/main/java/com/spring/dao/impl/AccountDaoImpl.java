package com.spring.dao.impl;

import com.spring.dao.AccountDao;
import com.spring.domain.Account;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.util.List;

/**
 * AccountDaoImpl class
 * Dao实现类一：适用于XML配置
 *
 * @author BowenWang
 * @date 2019/07/26
 */
public class AccountDaoImpl extends JdbcDaoSupport implements AccountDao {
    public Account findById(int id) {
        List<Account> accounts = getJdbcTemplate().query("select * from account where id = ?",
                new BeanPropertyRowMapper<Account>(Account.class), id);
        return accounts == null ? null : accounts.get(0);
    }

    public Account findByName(String name) {
        List<Account> accounts = getJdbcTemplate().query("select * from account where name = ?",
                new BeanPropertyRowMapper<Account>(Account.class), name);
        if (accounts == null) {
            return null;
        }
        if (accounts.size() > 1) {
            throw new RuntimeException("结果不唯一");
        }
        return accounts.get(0);
    }

    public void updateAccount(Account account) {
        getJdbcTemplate().update("update account set name = ?,money = ? where id = ?", account.getName(),
                account.getMoney(), account.getId());
    }

    public long totalAccount() {
        return getJdbcTemplate().queryForObject("select count(*) from account",Long.class);
    }
}

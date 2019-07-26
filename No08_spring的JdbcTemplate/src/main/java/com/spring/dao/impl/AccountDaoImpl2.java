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
 * Dao实现类一：适用于注解配置
 *
 * @author BowenWang
 * @date 2019/07/26
 */
@Repository("accountDao2")
public class AccountDaoImpl2 implements AccountDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Account findById(int id) {
        List<Account> accounts = jdbcTemplate.query("select * from account where id = ?",
                new BeanPropertyRowMapper<Account>(Account.class), id);
        return accounts == null ? null : accounts.get(0);
    }

    public Account findByName(String name) {
        List<Account> accounts = jdbcTemplate.query("select * from account where name = ?",
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
        jdbcTemplate.update("update account set name = ?,money = ? where id = ?", account.getName(),
                account.getMoney(), account.getId());
    }

    public long totalAccount() {
        return jdbcTemplate.queryForObject("select count(*) from account",Long.class);
    }
}

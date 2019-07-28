package config;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * TransactionConfig class
 * 事务管理配置类
 *
 * @author BowenWang
 * @date 2019/07/28
 */
public class TransactionConfig {

    /**
     * 创建事务管理器
     * @param dataSource
     * @return
     */
    @Bean("transactionManager")
    public PlatformTransactionManager createPlatformTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}

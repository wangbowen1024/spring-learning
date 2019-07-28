package config;

import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * SpringConfiguration class
 * 主配置类
 *
 * @author BowenWang
 * @date 2019/07/28
 */
@Configuration
@ComponentScan("com.spring")
@Import({JdbcConfig.class, TransactionConfig.class})
@PropertySources({@PropertySource("jdbcConfig.properties")})
@EnableTransactionManagement
public class SpringConfiguration {
}

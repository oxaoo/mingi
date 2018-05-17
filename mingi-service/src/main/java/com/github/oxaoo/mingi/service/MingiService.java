package com.github.oxaoo.mingi.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**
 * @author Alexander Kuleshov
 * @version 0.2.0
 * @since 10.05.2018
 */
@SpringBootApplication(scanBasePackages = "com.github.oxaoo.mingi")
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
public class MingiService {
    public static void main(String[] args) {
        SpringApplication.run(MingiService.class, args);
    }

}

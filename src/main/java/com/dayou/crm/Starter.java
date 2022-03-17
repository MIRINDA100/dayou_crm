package com.dayou.crm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Description:
 * @author: dayou
 * @create: 2022-03-06 10:55
 */
@MapperScan("com.dayou.crm.dao")
@SpringBootApplication
@EnableScheduling
public class Starter {

    public static void main(String[] args) {
        SpringApplication.run(Starter.class);
    }
}

package com.cy;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author cy
 * @version 1.0
 */
@Slf4j
@SpringBootApplication
@MapperScan("com.cy.mapper")
@EnableScheduling
public class HrApplication {
    public static void main(String[] args) {
        SpringApplication.run(HrApplication.class, args);
        log.info("项目启动成功...");
    }
}

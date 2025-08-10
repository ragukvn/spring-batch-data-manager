package com.ragukvn.data.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringBatchDataManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchDataManagerApplication.class, args);
    }

}

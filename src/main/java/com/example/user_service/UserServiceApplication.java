package com.example.user_service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class UserServiceApplication {

    private static final Logger LOGGER = LogManager.getLogger(UserServiceApplication.class);

    public static void main(String[] args) {

        LOGGER.info("Start server");
        SpringApplication.run(UserServiceApplication.class, args);
    }

    // @Bean
    // NewTopic logUserSerVice() {
    // return new NewTopic(ConstantUtil.KAFKA_TOPIC_LOG_USER_SERVICE, 2, (short) 1);
    // }
}

package com.fast.pagestream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan("com.fast.pagestream")
@EnableMongoRepositories("com.fast.pagestream.mvc.dao")
@EnableAutoConfiguration
@EnableScheduling
public class PageStreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(PageStreamApplication.class, args);
    }
}

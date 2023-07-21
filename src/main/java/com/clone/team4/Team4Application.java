package com.clone.team4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Team4Application {

    public static void main(String[] args) {
        SpringApplication.run(Team4Application.class, args);
    }

}

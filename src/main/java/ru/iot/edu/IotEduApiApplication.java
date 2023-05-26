package ru.iot.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class IotEduApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(IotEduApiApplication.class, args);
    }

}

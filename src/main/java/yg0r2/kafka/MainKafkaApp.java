package yg0r2.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MainKafkaApp {

    public static void main(String[] args) {
        SpringApplication.run(MainKafkaApp.class, args);
    }

}

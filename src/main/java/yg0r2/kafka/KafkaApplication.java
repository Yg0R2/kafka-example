package yg0r2.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "yg0r2")
public class KafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaApplication.class);
    }

}

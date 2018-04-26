package yg0r2.tmp.kafka;

import java.util.concurrent.CountDownLatch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CountDownLatchConfiguration {

    private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(1);

    @Bean
    public CountDownLatch countDownLatch() {
        return COUNT_DOWN_LATCH;
    }

}

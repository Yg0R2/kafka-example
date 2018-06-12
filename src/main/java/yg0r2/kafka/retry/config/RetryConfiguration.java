package yg0r2.kafka.retry.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import yg0r2.kafka.retry.CircuitBreakerRetryPolicy;
import yg0r2.kafka.retry.RetryPolicy;
import yg0r2.kafka.retry.TimeRetryPolicy;

@Configuration
public class RetryConfiguration {

    @Value("${kafka.retry.max.seconds}")
    private long retryMaxSeconds;

    @Bean
    public RetryPolicy retryPolicy() {
        return new CircuitBreakerRetryPolicy(new TimeRetryPolicy(retryMaxSeconds));
    }

}

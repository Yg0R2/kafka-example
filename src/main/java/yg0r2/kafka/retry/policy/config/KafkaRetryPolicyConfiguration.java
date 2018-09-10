package yg0r2.kafka.retry.policy.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.ImmutableList;

import yg0r2.external.exception.UnrecoverableDataSourceException;
import yg0r2.kafka.retry.policy.CircuitBreakerRetryPolicy;
import yg0r2.kafka.retry.policy.ExcludedExceptionRetryPolicy;
import yg0r2.kafka.retry.policy.FixEndDateTimeRetryPolicy;
import yg0r2.kafka.retry.policy.RetryPolicy;

@Configuration
public class KafkaRetryPolicyConfiguration {

    @Value("${kafka.retry.initial.retention.seconds}")
    private long initialRetentionSeconds;
    @Value("${kafka.retry.retention.max.seconds}")
    private long retentionMaxSeconds;

    @Bean
    public RetryPolicy retryPolicy() {
        RetryPolicy fixEndDateTimeRetryPolicy = new FixEndDateTimeRetryPolicy(retentionMaxSeconds);
        RetryPolicy circuitBreakerRetryPolicy = new CircuitBreakerRetryPolicy(fixEndDateTimeRetryPolicy, initialRetentionSeconds);

        return new ExcludedExceptionRetryPolicy(circuitBreakerRetryPolicy, excludedExceptions());
    }

    private List<Class<? extends Throwable>> excludedExceptions() {
        return new ImmutableList.Builder<Class<? extends Throwable>>()
            .add(UnrecoverableDataSourceException.class)
            .build();
    }

}

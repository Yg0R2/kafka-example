package yg0r2.kafka.scheduler.config;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import yg0r2.kafka.consumer.BookingEmailRequestConsumer;
import yg0r2.kafka.scheduler.CommonThreadFactory;

@Configuration
public class FastLaneBookingEmailRequestSchedulingConfiguration {

    private static final String CONSUMER_BEAN_NAME = "fastLaneBookingEmailRequestConsumer";
    private static final String THREAD_BASE_NAME = CONSUMER_BEAN_NAME + "Thread";

    @Value("${kafka.fastLane.poll.interval.ms}")
    private long fastLanePollInterval;
    @Value("${kafka.fastLane.poll.initialDelay.ms}")
    private long fastLaneInitialDelay;
    @Value("${kafka.fastLane.poll.pool.size}")
    private int fastLanePollPoolSize;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean("fastLaneScheduledThreadPoolExecutor")
    public ScheduledThreadPoolExecutor fastLaneScheduledThreadPoolExecutor() {
        ThreadFactory threadFactory = new CommonThreadFactory(THREAD_BASE_NAME);

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(fastLanePollPoolSize, threadFactory);

        executor.setMaximumPoolSize(fastLanePollPoolSize);

        for (int i = 0; i < fastLanePollPoolSize; i++) {
            BookingEmailRequestConsumer fastLaneBookingEmailRequestConsumer =
                (BookingEmailRequestConsumer) applicationContext.getBean(CONSUMER_BEAN_NAME);

            executor.scheduleAtFixedRate(
                () -> fastLaneBookingEmailRequestConsumer.poll(),
                fastLaneInitialDelay,
                fastLanePollInterval,
                TimeUnit.MILLISECONDS);
        }

        return executor;
    }

}

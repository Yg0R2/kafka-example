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
public class SlowLaneBookingEmailRequestSchedulingConfiguration {

    private static final String SLOW_LANE_CONSUMER_BEAN_NAME = "slowLaneBookingEmailRequestConsumer";
    private static final String SLOW_LANE_THREAD_BASE_NAME = SLOW_LANE_CONSUMER_BEAN_NAME + "Thread";

    @Value("${kafka.slowLane.poll.interval.ms}")
    private long slowLanePollInterval;
    @Value("${kafka.slowLane.poll.initialDelay.ms}")
    private long slowLaneInitialDelay;
    @Value("${kafka.slowLane.poll.pool.size}")
    private int slowLanePollPoolSize;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean("slowLaneScheduledThreadPoolExecutor")
    public ScheduledThreadPoolExecutor slowLaneScheduledThreadPoolExecutor() {
        ThreadFactory threadFactory = new CommonThreadFactory(SLOW_LANE_THREAD_BASE_NAME);

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(slowLanePollPoolSize, threadFactory);

        executor.setMaximumPoolSize(slowLanePollPoolSize);

        for (int i = 0; i < slowLanePollPoolSize; i++) {
            BookingEmailRequestConsumer slowLaneBookingEmailRequestConsumer =
                (BookingEmailRequestConsumer) applicationContext.getBean(SLOW_LANE_CONSUMER_BEAN_NAME);

            executor.scheduleAtFixedRate(
                () -> slowLaneBookingEmailRequestConsumer.poll(),
                slowLaneInitialDelay,
                slowLanePollInterval,
                TimeUnit.MILLISECONDS);
        }

        return executor;
    }

}

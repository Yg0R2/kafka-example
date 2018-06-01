package yg0r2.tmp.kafka.scheduler;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import yg0r2.tmp.kafka.consumer.BookingEmailRequestConsumer;

@Configuration
public class SlowLaneBookingEmailRequestSchedulingConfiguration {

    @Value("${kafka.slowLane.poll.interval.ms}")
    private long slowLanePollInterval;
    @Value("${kafka.slowLane.poll.initialDelay.ms}")
    private long slowLaneInitialDelay;
    @Value("${kafka.slowLane.poll.pool.size}")
    private int slowLanePollPoolSize;

    @Autowired
    @Qualifier(value = "slowLaneBookingEmailRequestConsumer")
    private BookingEmailRequestConsumer slowLaneBookingEmailRequestConsumer;

    @Bean("slowLaneScheduledThreadPoolExecutor")
    public ScheduledThreadPoolExecutor slowLaneScheduledThreadPoolExecutor() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(slowLanePollPoolSize);

        executor.setMaximumPoolSize(slowLanePollPoolSize);
        executor.scheduleAtFixedRate(
            () -> slowLaneBookingEmailRequestConsumer.poll(),
            slowLaneInitialDelay,
            slowLanePollInterval,
            TimeUnit.MILLISECONDS);

        return executor;
    }

}

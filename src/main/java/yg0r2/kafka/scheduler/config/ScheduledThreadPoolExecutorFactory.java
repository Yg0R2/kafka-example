package yg0r2.kafka.scheduler.config;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import yg0r2.kafka.consumer.KafkaMessageRecordConsumer;
import yg0r2.kafka.scheduler.KafkaThreadFactory;

@Component
class ScheduledThreadPoolExecutorFactory {

    public ScheduledThreadPoolExecutor create(ApplicationContext applicationContext, String beanName, int pollPoolSize,
        long initialDelay, long pollInterval) {

        ThreadFactory threadFactory = new KafkaThreadFactory(beanName + "Thread");

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(pollPoolSize, threadFactory);

        executor.setMaximumPoolSize(pollPoolSize);

        for (int i = 0; i < pollPoolSize; i++) {
            KafkaMessageRecordConsumer kafkaMessageRecordConsumer =
                applicationContext.getBean(beanName, KafkaMessageRecordConsumer.class);

            executor.scheduleAtFixedRate(
                () -> kafkaMessageRecordConsumer.poll(),
                initialDelay,
                pollInterval,
                TimeUnit.MILLISECONDS);
        }

        return executor;
    }

}

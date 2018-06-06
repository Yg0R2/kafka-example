package yg0r2.kafka.scheduler;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class CommonThreadFactory implements ThreadFactory {

    private static final String THREAD_NAME_PATTERN = "%s-%d";

    private final String baseThreadName;

    private AtomicInteger counter = new AtomicInteger();

    public CommonThreadFactory(String baseThreadName) {
        this.baseThreadName = baseThreadName;
    }

    @Override
    public Thread newThread(Runnable r) {
        String threadName = String.format(THREAD_NAME_PATTERN, baseThreadName, counter.getAndIncrement());

        return new Thread(r, threadName);
    }
}

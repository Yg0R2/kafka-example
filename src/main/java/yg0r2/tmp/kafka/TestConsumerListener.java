package yg0r2.tmp.kafka;

import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TestConsumerListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestConsumerListener.class);

    @Autowired
    private ErrorHandler errorHandler;
    @Autowired
    private RequestProcessor requestProcessor;
    @Autowired
    private Consumer<String, String> testConsumer;

    @Value("${kafka.slowLane.topic}")
    private String slowLaneTopic;

    private int noRecordsCounter;

    public void runConsumer() throws InterruptedException {
        while (true) {
            ConsumerRecords<String, String> consumerRecords = testConsumer.poll(10000L);

            if (consumerRecords.isEmpty()) {
                noRecordsCounter++;

                if (noRecordsCounter > 100) {
                    break;
                }
                else {
                    continue;
                }
            }

            Map<TopicPartition, Long> query = new HashMap<>();
            query.put(
                    new TopicPartition(slowLaneTopic, 0),
                    Instant.now().plusSeconds(5).toEpochMilli()
            );

            Map<TopicPartition, OffsetAndTimestamp> result = testConsumer.offsetsForTimes(query);
            result.entrySet().stream()
                .forEach(entry -> testConsumer.seek(entry.getKey(), entry.getValue().offset()));

            consumerRecords.forEach(this::run);

            testConsumer.commitSync();
        }

        testConsumer.close();

        LOGGER.info("DONE");
    }

    private void run(ConsumerRecord<String, String> consumerRecord) {
        try {
            requestProcessor.handleRequest(consumerRecord.value());
        }
        catch (Exception e) {
            errorHandler.handle(e, consumerRecord);
        }
    }

}

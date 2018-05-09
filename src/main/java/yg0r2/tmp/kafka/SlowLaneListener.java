package yg0r2.tmp.kafka;

import java.util.List;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;

@Component
public class SlowLaneListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlowLaneListener.class);

    @Value("${kafka.slowLane.topic}")
    private String slowLaneTopic;

    @Autowired
    private RequestProcessor requestProcessor;
    @Autowired
    private Consumer<String, String> slowLaneConsumer;

    //@Scheduled(fixedDelay=5000)
    public void poll() {
        ConsumerRecords<String, String> records = slowLaneConsumer.poll(1000);

        List<ConsumerRecord<String, String>> result = new ImmutableList.Builder<ConsumerRecord<String, String>>()
            .addAll(records.records(slowLaneTopic))
            .build();

        receive(result);
    }

    private void receive(List<ConsumerRecord<String, String>> records) {
        records.stream().forEach(this::processRecord);
    }

    private void processRecord(ConsumerRecord<String, String> record) {
        LOGGER.info("Record consumed from topic={} partition={} offset={} payload={}",
            record.topic(), record.partition(), record.offset(), record.value());

        requestProcessor.handleRequest(record.value());
    }

}

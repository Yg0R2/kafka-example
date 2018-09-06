package yg0r2.kafka.consumer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.UUID;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.context.junit4.SpringRunner;

import yg0r2.kafka.domain.KafkaMessageRecord;
import yg0r2.kafka.domain.RequestCorrelationId;
import yg0r2.kafka.processor.KafkaMessageRecordProcessor;
import yg0r2.kafka.producer.KafkaMessageRecordProducer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FastLaneKafkaMessageRecordConsumerTest {

    private static final String TOPIC = "fast-lane-topic";
    private static final long POLL_TIMEOUT = 3000L;

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, 1, TOPIC);

    @Autowired
    private Consumer<RequestCorrelationId, KafkaMessageRecord> fastLaneKafkaConsumer;
    @Autowired
    private KafkaMessageRecordProducer fastLaneKafkaMessageRecordProducer;

    @Mock
    private KafkaMessageRecordProcessor kafkaMessageRecordProcessor;

    private KafkaMessageRecordConsumer underTest;

    @Before
    public void setUp() {
        underTest = new KafkaMessageRecordConsumer(fastLaneKafkaConsumer, kafkaMessageRecordProcessor, TOPIC, POLL_TIMEOUT);
    }

    @Test
    public void testShouldReturnProperResponse() {
        // GIVEN
        KafkaMessageRecord kafkaMessageRecord = createKafkaMessageRecord("requestData");
        fastLaneKafkaMessageRecordProducer.submitRequest(kafkaMessageRecord);

        // WHEN
        doNothing().when(kafkaMessageRecordProcessor).processRecord(isA(ConsumerRecord.class));

        underTest.poll();

        // THEN
        ArgumentCaptor<ConsumerRecord> argumentCaptor = ArgumentCaptor.forClass(ConsumerRecord.class);

        verify(kafkaMessageRecordProcessor).processRecord(argumentCaptor.capture());
        verifyNoMoreInteractions(kafkaMessageRecordProcessor);

        RequestCorrelationId requestCorrelationId = createRequestCorrelationId(kafkaMessageRecord);

        assertEquals(requestCorrelationId, argumentCaptor.getValue().key());
        assertEquals(kafkaMessageRecord, argumentCaptor.getValue().value());
    }

    private KafkaMessageRecord createKafkaMessageRecord(String request) {
        return new KafkaMessageRecord.Builder()
            .withRequestId(UUID.randomUUID())
            .withRequest(request)
            .withTimestamp(System.nanoTime())
            .build();
    }

    private RequestCorrelationId createRequestCorrelationId(KafkaMessageRecord kafkaMessageRecord) {
        return new RequestCorrelationId(kafkaMessageRecord.getRequestId(), kafkaMessageRecord.getTimestamp());
    }

}
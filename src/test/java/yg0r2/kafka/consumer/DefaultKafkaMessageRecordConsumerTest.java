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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import yg0r2.kafka.domain.KafkaMessageRecord;
import yg0r2.kafka.domain.RequestCorrelationId;
import yg0r2.kafka.processor.KafkaMessageRecordProcessor;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DefaultKafkaMessageRecordConsumerTest {

    private static final String TEST_TOPIC = "test-topic";
    private static final long POLL_TIMEOUT = 3000L;

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, 1, TEST_TOPIC);

    @Autowired
    private Consumer<RequestCorrelationId, KafkaMessageRecord> kafkaConsumer;
    @Autowired
    private KafkaTemplate<RequestCorrelationId, KafkaMessageRecord> kafkaTemplate;

    @Mock
    private KafkaMessageRecordProcessor kafkaMessageRecordProcessor;

    @InjectMocks
    private DefaultKafkaMessageRecordConsumer underTest;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(underTest, "pollTimeout", POLL_TIMEOUT);
        ReflectionTestUtils.setField(underTest, "kafkaConsumer", kafkaConsumer);
    }

    @Test
    public void testShouldReturnProperResponse() {
        // GIVEN
        RequestCorrelationId requestCorrelationId = createRequestCorrelationId();
        KafkaMessageRecord kafkaMessageRecord = createKafkaMessageRecord("requestData");
        kafkaTemplate.send(TEST_TOPIC, requestCorrelationId, kafkaMessageRecord);

        // WHEN
        doNothing().when(kafkaMessageRecordProcessor).processRecord(isA(ConsumerRecord.class));

        underTest.poll();

        // THEN
        ArgumentCaptor<ConsumerRecord> argumentCaptor = ArgumentCaptor.forClass(ConsumerRecord.class);

        verify(kafkaMessageRecordProcessor).processRecord(argumentCaptor.capture());
        verifyNoMoreInteractions(kafkaMessageRecordProcessor);

        assertEquals(requestCorrelationId, argumentCaptor.getValue().key());
        assertEquals(kafkaMessageRecord, argumentCaptor.getValue().value());
    }

    private RequestCorrelationId createRequestCorrelationId() {
        return new RequestCorrelationId(UUID.randomUUID(), System.nanoTime());
    }

    private KafkaMessageRecord createKafkaMessageRecord(String request) {
        return new KafkaMessageRecord.Builder()
            .withRequest(request)
            .build();
    }

}

package yg0r2.kafka.consumer;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.UUID;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.context.junit4.SpringRunner;

import yg0r2.external.RequestProcessor;
import yg0r2.kafka.domain.Request;
import yg0r2.kafka.producer.KafkaMessageRecordProducer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FastLaneKafkaMessageRecordConsumerTest {

    private static final String TOPIC = "fast-lane-topic";
    private static final long POLL_TIMEOUT = 5000L;

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(2, true, 1, TOPIC);

    @Autowired
    private KafkaMessageRecordProducer<Request> fastLaneKafkaMessageRecordProducer;

    @SpyBean(name = "mockRequestProcessor")
    private RequestProcessor requestProcessor;

    @Test
    public void testShouldReturnProperResponse() throws InterruptedException {
        // GIVEN
        Request request = createRequest("requestData");
        fastLaneKafkaMessageRecordProducer.submit(request);

        // WHEN
        doNothing().when(requestProcessor).processRequest(request);

        // waiting for scheduled poll
        Thread.sleep(5000);

        // THEN
        verify(requestProcessor).processRequest(request);
        verifyNoMoreInteractions(requestProcessor);
    }

    private Request createRequest(String value) {
        return new Request.Builder()
            .withRequestId(UUID.randomUUID())
            .withTimestamp(System.nanoTime())
            .withValue(value)
            .build();
    }

}

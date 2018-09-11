package yg0r2.kafka;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.apache.kafka.clients.consumer.Consumer;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import yg0r2.kafka.aspect.KafkaMessageRecordProducerErrorHandler;
import yg0r2.kafka.domain.KafkaMessageRecord;
import yg0r2.kafka.domain.Request;
import yg0r2.kafka.domain.RequestCorrelationId;
import yg0r2.kafka.producer.KafkaMessageRecordProducer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KafkaApplication.class)
@DirtiesContext
@Import(KafkaMessageRecordProducerErrorHandler.class)
@WebAppConfiguration
public class KafkaApplicationTest {

    private static final String FAST_LANE_TOPIC = "fast-lane-topic";
    private static final String SLOW_LANE_TOPIC = "slow-lane-topic";

    @SpyBean(name = "fastLaneKafkaMessageRecordProducer")
    private KafkaMessageRecordProducer<Request> fastLaneKafkaMessageRecordProducer;
//    @SpyBean(name = "fastLaneKafkaConsumer")
//    private Consumer<RequestCorrelationId, KafkaMessageRecord> fastLaneKafkaConsumer;
    @SpyBean(name = "slowLaneKafkaMessageRecordProducer")
    private KafkaMessageRecordProducer<KafkaMessageRecord> slowLaneKafkaMessageRecordProducer;
//    @SpyBean(name = "slowLaneKafkaConsumer")
//    private Consumer<RequestCorrelationId, KafkaMessageRecord> slowLaneKafkaConsumer;

    @ClassRule
    public static KafkaEmbedded embeddedFastLaneKafka = new KafkaEmbedded(4, true, 2, FAST_LANE_TOPIC, SLOW_LANE_TOPIC);

    @Test
    public void testStartup() {
        assertEquals(1, embeddedFastLaneKafka.getKafkaServers().size());
        assertEquals(1, embeddedFastLaneKafka.getBrokerAddresses().length);
        assertEquals(1, embeddedFastLaneKafka.getPartitionsPerTopic());
        assertNotNull(embeddedFastLaneKafka.getZookeeper());
    }

    @Test
    public void testRun() throws InterruptedException {
        Request request = createRequest("0000000");
        fastLaneKafkaMessageRecordProducer.submit(request);

        Request request2 = createRequest("1111111");
        //fastLaneKafkaMessageRecordProducer.submit(request2);

        while (true){
            if (false) {
                break;
            }
        }

        Thread.sleep(10000);

        verify(fastLaneKafkaMessageRecordProducer).submit(request);
        //verify(fastLaneKafkaConsumer, times(9)).poll(anyLong());
        verify(slowLaneKafkaMessageRecordProducer, times(3)).submit(any(KafkaMessageRecord.class));
        //verify(slowLaneKafkaConsumer, times(3)).poll(anyLong());
    }

    private Request createRequest(String value) {
        return new Request.Builder()
            .withRequestId(UUID.randomUUID())
            .withTimestamp(System.nanoTime())
            .withValue(value)
            .build();
    }

}

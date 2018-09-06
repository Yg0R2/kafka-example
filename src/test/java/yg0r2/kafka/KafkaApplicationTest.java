package yg0r2.kafka;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class KafkaApplicationTest {

    private static final String TEST_TOPIC = "test-topic";

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, 1, TEST_TOPIC);

    @Test
    public void testStartup() {
        assertEquals(1, embeddedKafka.getKafkaServers().size());
        assertEquals(1, embeddedKafka.getBrokerAddresses().length);
        assertEquals(1, embeddedKafka.getPartitionsPerTopic());
        assertNotNull(embeddedKafka.getZookeeper());
    }

}

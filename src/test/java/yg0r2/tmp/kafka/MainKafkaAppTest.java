package yg0r2.tmp.kafka;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class MainKafkaAppTest {

    private static final String FAST_LANE_TOPIC = "tmp-fastLane";
    private static final String SLOW_LANE_TOPIC = "tmp-slowLane";

    @ClassRule
    //public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, FAST_LANE_TOPIC);
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(2, true, 3, FAST_LANE_TOPIC, SLOW_LANE_TOPIC);


    @Autowired
    private Sender sender;

    @Test
    public void testReceive() throws Exception {
        sender.send(FAST_LANE_TOPIC, "Hello Spring Kafka!");
        Thread.sleep(100);
        sender.send(FAST_LANE_TOPIC, "Hello Spring Kafka22222!");


        Thread.sleep(60000);
    }

}

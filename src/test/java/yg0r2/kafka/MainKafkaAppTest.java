package yg0r2.kafka;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import yg0r2.kafka.producer.Producer;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class MainKafkaAppTest {

    private static final String FAST_LANE_TOPIC = "tmp-fastLane";
    private static final String SLOW_LANE_TOPIC = "tmp-slowLane";
    private static final Logger LOGGER = LoggerFactory.getLogger(MainKafkaAppTest.class);

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(5, true, 3, FAST_LANE_TOPIC, SLOW_LANE_TOPIC);

    @Autowired
    private Producer producer;

    @Test
    public void testReceive() throws Exception {
        LOGGER.info("Waiting for startup finish");
        Thread.sleep(3000);

        LOGGER.info("Send messages to " + FAST_LANE_TOPIC);
        producer.send(FAST_LANE_TOPIC, "000000000000");
        producer.send(FAST_LANE_TOPIC, "111111111111");
        producer.send(FAST_LANE_TOPIC, "222222222222");
        producer.send(FAST_LANE_TOPIC, "333333333333");
        producer.send(FAST_LANE_TOPIC, "444444444444");
        producer.send(FAST_LANE_TOPIC, "555555555555");

        while (true) {

        }
        //Thread.sleep(60000);
    }

}

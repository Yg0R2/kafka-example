package yg0r2.tmp.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SlowLaneResubmitProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlowLaneResubmitProcessor.class);

    @Value("${kafka.slowLane.topic}")
    private String slowLaneTopic;

    @Autowired
    private Sender sender;

    public void resubmit(String payload) {
        LOGGER.info("jeee SlowLaneResubmitProcessor");

        sender.send(slowLaneTopic, payload);
    }
}

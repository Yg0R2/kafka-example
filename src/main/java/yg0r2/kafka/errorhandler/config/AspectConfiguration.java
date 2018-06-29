package yg0r2.kafka.errorhandler.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import yg0r2.kafka.errorhandler.RequestResubmittingErrorHandlerAspect;
import yg0r2.kafka.producer.DefaultSlowLaneBookingEmailRequestSubmitter;
import yg0r2.kafka.retry.KafkaRetryService;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AspectConfiguration {

    @Autowired
    private KafkaRetryService kafkaRetryService;
    @Autowired
    private DefaultSlowLaneBookingEmailRequestSubmitter slowLaneBookingEmailRequestSubmitter;

    @Bean
    public RequestResubmittingErrorHandlerAspect requestResubmittingErrorHandlerAspect() {
        return new RequestResubmittingErrorHandlerAspect(kafkaRetryService, slowLaneBookingEmailRequestSubmitter);
    }
}

package yg0r2.tmp.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import yg0r2.tmp.kafka.producer.DefaultSlowLaneBookingEmailRequestSubmitter;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AspectConfiguration {

    @Autowired
    private DefaultSlowLaneBookingEmailRequestSubmitter defaultSlowLaneBookingEmailRequestSubmitter;

    @Bean
    public RequestResubmittingErrorHandlerAspect requestResubmittingErrorHandlerAspect() {
        return new RequestResubmittingErrorHandlerAspect(defaultSlowLaneBookingEmailRequestSubmitter);
    }
}

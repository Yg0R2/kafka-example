package yg0r2.tmp.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AspectConfiguration {

    @Autowired
    private SlowLaneResubmitProcessor slowLaneResubmitProcessor;

    @Bean
    public ErrorHandlerAspect errorHandlerAspect() {
        return new ErrorHandlerAspect(slowLaneResubmitProcessor);
    }

}

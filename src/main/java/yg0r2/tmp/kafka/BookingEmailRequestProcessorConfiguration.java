package yg0r2.tmp.kafka;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class BookingEmailRequestProcessorConfiguration {

    @Bean("slowLaneBookingEmailRequestProcessor")
    public BookingEmailRequestProcessor slowLaneBookingEmailRequestProcessor() {
        return new BookingEmailRequestProcessor();
    }

    @Bean("fastLaneBookingEmailRequestProcessor")
    public BookingEmailRequestProcessor fastLaneBookingEmailRequestProcessor() {
        return new BookingEmailRequestProcessor();
    }

}

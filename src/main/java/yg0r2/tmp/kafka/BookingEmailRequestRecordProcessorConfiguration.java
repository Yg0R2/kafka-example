package yg0r2.tmp.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookingEmailRequestRecordProcessorConfiguration {

    @Autowired
    private BookingEmailRequestProcessorService bookingEmailRequestProcessorService;

    @Bean("slowLaneBookingEmailRequestRecordProcessor")
    public BookingEmailRequestRecordProcessor slowLaneBookingEmailRequestRecordProcessor() {
        return new BookingEmailRequestRecordProcessor(bookingEmailRequestProcessorService);
    }

    @Bean("fastLaneBookingEmailRequestRecordProcessor")
    public BookingEmailRequestRecordProcessor fastLaneBookingEmailRequestRecordProcessor() {
        return new BookingEmailRequestRecordProcessor(bookingEmailRequestProcessorService);
    }

}

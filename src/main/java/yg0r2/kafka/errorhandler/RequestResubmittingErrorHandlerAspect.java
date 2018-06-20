package yg0r2.kafka.errorhandler;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yg0r2.kafka.domain.KafkaMessageRecord;
import yg0r2.kafka.producer.DefaultSlowLaneBookingEmailRequestSubmitter;

@Aspect
public class RequestResubmittingErrorHandlerAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestResubmittingErrorHandlerAspect.class);

    private final DefaultSlowLaneBookingEmailRequestSubmitter slowLaneBookingEmailRequestSubmitter;

    public RequestResubmittingErrorHandlerAspect(DefaultSlowLaneBookingEmailRequestSubmitter slowLaneBookingEmailRequestSubmitter) {
        this.slowLaneBookingEmailRequestSubmitter = slowLaneBookingEmailRequestSubmitter;
    }

    @Around("execution(* yg0r2.kafka.service.BookingEmailRequestRecordProcessor.processRecord(..))")
    public void executeDefendedRequest(ProceedingJoinPoint proceedingJoinPoint) {
        try {
            proceedingJoinPoint.proceed();
        }
        catch (Throwable throwable) {
            KafkaMessageRecord kafkaMessageRecord = getKafkaMessageRecord(proceedingJoinPoint);

            LOGGER.info("Resubmitting failed request payload={}", kafkaMessageRecord.getPayload());
            slowLaneBookingEmailRequestSubmitter.submitEmailRequest(kafkaMessageRecord);
        }
    }

    private KafkaMessageRecord getKafkaMessageRecord(ProceedingJoinPoint proceedingJoinPoint){
        return ((ConsumerRecord<String, KafkaMessageRecord>) proceedingJoinPoint.getArgs()[0]).value();
    }

}

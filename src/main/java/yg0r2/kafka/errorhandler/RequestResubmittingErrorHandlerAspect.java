package yg0r2.kafka.errorhandler;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yg0r2.kafka.domain.KafkaMessageRecord;
import yg0r2.kafka.producer.DefaultSlowLaneBookingEmailRequestSubmitter;
import yg0r2.kafka.retry.KafkaRetryService;

@Aspect
public class RequestResubmittingErrorHandlerAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestResubmittingErrorHandlerAspect.class);

    private final KafkaRetryService kafkaRetryService;
    private final DefaultSlowLaneBookingEmailRequestSubmitter slowLaneBookingEmailRequestSubmitter;

    public RequestResubmittingErrorHandlerAspect(KafkaRetryService kafkaRetryService, DefaultSlowLaneBookingEmailRequestSubmitter slowLaneBookingEmailRequestSubmitter) {
        this.kafkaRetryService = kafkaRetryService;
        this.slowLaneBookingEmailRequestSubmitter = slowLaneBookingEmailRequestSubmitter;
    }

    @Around("execution(* yg0r2.kafka.service.BookingEmailRequestRecordProcessor.processRecord(..))")
    public void executeDefendedRequest(ProceedingJoinPoint proceedingJoinPoint) {
        KafkaMessageRecord kafkaMessageRecord = getKafkaMessageRecord(proceedingJoinPoint);

        if (kafkaRetryService.isAllowedToExecuteRetry(kafkaMessageRecord)) {
            try {
                proceedingJoinPoint.proceed();
            }
            catch (Throwable throwable) {
                kafkaRetryService.handleRetry(kafkaMessageRecord, throwable);
            }
        }
        else {
            slowLaneBookingEmailRequestSubmitter.submitEmailRequest(kafkaMessageRecord);
        }
    }

    private KafkaMessageRecord getKafkaMessageRecord(JoinPoint joinPoint){
        return ((ConsumerRecord<String, KafkaMessageRecord>) joinPoint.getArgs()[0]).value();
    }

}

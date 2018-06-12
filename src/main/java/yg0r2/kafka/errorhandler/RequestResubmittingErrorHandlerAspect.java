package yg0r2.kafka.errorhandler;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yg0r2.kafka.domain.KafkaMessageRecord;
import yg0r2.kafka.producer.DefaultSlowLaneBookingEmailRequestSubmitter;
import yg0r2.kafka.retry.RetryPolicy;
import yg0r2.kafka.retry.domain.RetryContext;

@Aspect
public class RequestResubmittingErrorHandlerAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestResubmittingErrorHandlerAspect.class);

    private final DefaultSlowLaneBookingEmailRequestSubmitter slowLaneBookingEmailRequestSubmitter;
    private final RetryPolicy retryPolicy;

    public RequestResubmittingErrorHandlerAspect(DefaultSlowLaneBookingEmailRequestSubmitter slowLaneBookingEmailRequestSubmitter, RetryPolicy retryPolicy) {
        this.slowLaneBookingEmailRequestSubmitter = slowLaneBookingEmailRequestSubmitter;
        this.retryPolicy = retryPolicy;
    }

    @Around("execution(* yg0r2.kafka.service.BookingEmailRequestProcessorService.*(..))")
    public void executeDefendedRequest(ProceedingJoinPoint proceedingJoinPoint) {
        try {
            proceedingJoinPoint.proceed();
        }
        catch (Throwable throwable) {
            KafkaMessageRecord<String> kafkaMessageRecord = (KafkaMessageRecord<String>) proceedingJoinPoint.getArgs()[0];

            RetryContext retryContext = createRetryContext(kafkaMessageRecord, throwable);
            if (retryPolicy.canRetry(retryContext)) {
                resubmit(kafkaMessageRecord);
            }
            else {
                LOGGER.info("Retry doesn't allow for payload={}", kafkaMessageRecord.getPayload());
            }
        }
    }

    private RetryContext createRetryContext(KafkaMessageRecord<String> kafkaMessageRecord, Throwable throwable) {
        return new RetryContext.Builder()
            .withMessageRecord(kafkaMessageRecord)
            .withThrowable(throwable)
            .build();
    }

    private void resubmit(KafkaMessageRecord<String> kafkaMessageRecord) {
        LOGGER.info("Resubmitting failed request payload={}", kafkaMessageRecord.getPayload());

        slowLaneBookingEmailRequestSubmitter.submitEmailRequest(kafkaMessageRecord);
    }

}
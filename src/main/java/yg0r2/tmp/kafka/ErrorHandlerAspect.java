package yg0r2.tmp.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

@Aspect
public class ErrorHandlerAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandlerAspect.class);

    private SlowLaneResubmitProcessor slowLaneResubmitProcessor;

    public ErrorHandlerAspect(SlowLaneResubmitProcessor slowLaneResubmitProcessor) {
        this.slowLaneResubmitProcessor = slowLaneResubmitProcessor;
    }

    @Around("execution(* yg0r2.tmp.kafka.BookingEmailRequestProcessor.*(..))")
    public void executeDefendedRequest(ProceedingJoinPoint proceedingJoinPoint) {
        LOGGER.info("Aspect error happened.");

        try {
            proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            slowLaneResubmitProcessor.resubmit(getPayload(proceedingJoinPoint));
        }
    }

    private String getPayload(ProceedingJoinPoint proceedingJoinPoint) {
        return ((ImmutableList<ConsumerRecord<String, String>>) proceedingJoinPoint.getArgs()[0]).get(0).value();
    }

}

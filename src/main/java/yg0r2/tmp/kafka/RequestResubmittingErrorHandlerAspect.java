package yg0r2.tmp.kafka;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yg0r2.tmp.kafka.producer.DefaultSlowLaneBookingEmailRequestSubmitter;

@Aspect
public class RequestResubmittingErrorHandlerAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestResubmittingErrorHandlerAspect.class);

    private final DefaultSlowLaneBookingEmailRequestSubmitter slowLaneBookingEmailRequestSubmitter;

    public RequestResubmittingErrorHandlerAspect(DefaultSlowLaneBookingEmailRequestSubmitter slowLaneBookingEmailRequestSubmitter) {
        this.slowLaneBookingEmailRequestSubmitter = slowLaneBookingEmailRequestSubmitter;
    }

    @Around("execution(* yg0r2.tmp.kafka.BookingEmailRequestProcessorService.*(..))")
    public void executeDefendedRequest(ProceedingJoinPoint proceedingJoinPoint) {
        try {
            proceedingJoinPoint.proceed();
        }
        catch (Throwable throwable) {
            String payload = (String) proceedingJoinPoint.getArgs()[0];

            LOGGER.info("Resubmitting failed request payload={}", payload);
            slowLaneBookingEmailRequestSubmitter.submitEmailRequest(payload);
        }
    }

}

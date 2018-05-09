package yg0r2.tmp.kafka;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class ErrorHandlerAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandlerAspect.class);

    private SlowLaneResubmitProcessor slowLaneResubmitProcessor;

    public ErrorHandlerAspect(SlowLaneResubmitProcessor slowLaneResubmitProcessor) {
        this.slowLaneResubmitProcessor = slowLaneResubmitProcessor;
    }

    @Around("execution(* yg0r2.tmp.kafka.RequestProcessor.*(..))")
    public void executeDefendedRequest(ProceedingJoinPoint proceedingJoinPoint) {
        LOGGER.info("Aspect error happened.");

        try {
            proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            slowLaneResubmitProcessor.resubmit((String) proceedingJoinPoint.getArgs()[0]);
        }
    }

}

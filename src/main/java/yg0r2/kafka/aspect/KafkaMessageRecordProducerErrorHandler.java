package yg0r2.kafka.aspect;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import yg0r2.kafka.domain.KafkaMessageRecord;
import yg0r2.kafka.domain.RequestCorrelationId;
import yg0r2.kafka.producer.SlowLaneKafkaMessageRecordProducer;

@Aspect
public class KafkaMessageRecordProducerErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageRecordProducerErrorHandler.class);

    @Autowired
    private SlowLaneKafkaMessageRecordProducer slowLaneKafkaMessageRecordProducer;

    @Around("execution(* yg0r2.kafka.processor.KafkaMessageRecordProcessor.processRecord(..))")
    public void executeDefendedRequest(ProceedingJoinPoint proceedingJoinPoint) {
        KafkaMessageRecord kafkaMessageRecord = getKafkaMessageRecord(proceedingJoinPoint);

        try {
            proceedingJoinPoint.proceed();
        }
        catch (Throwable throwable) {
            LOGGER.error("Resubmitting failed request.", throwable);

            slowLaneKafkaMessageRecordProducer.submit(kafkaMessageRecord);
        }
    }

    private KafkaMessageRecord getKafkaMessageRecord(JoinPoint joinPoint) {
        return ((ConsumerRecord<RequestCorrelationId, KafkaMessageRecord>) joinPoint.getArgs()[0]).value();
    }

}

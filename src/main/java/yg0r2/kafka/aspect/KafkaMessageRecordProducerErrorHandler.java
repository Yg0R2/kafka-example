package yg0r2.kafka.aspect;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import yg0r2.kafka.domain.KafkaMessageRecord;
import yg0r2.kafka.domain.RequestCorrelationId;
import yg0r2.kafka.producer.SlowLaneKafkaMessageRecordProducer;
import yg0r2.kafka.retry.KafkaRetryService;

@Aspect
public class KafkaMessageRecordProducerErrorHandler {

    @Autowired
    private KafkaRetryService kafkaRetryService;
    @Autowired
    private SlowLaneKafkaMessageRecordProducer slowLaneKafkaMessageRecordProducer;

    @Around("execution(* yg0r2.kafka.processor.KafkaMessageRecordProcessor.processRecord(..))")
    public void executeDefendedRequest(ProceedingJoinPoint proceedingJoinPoint) {
        KafkaMessageRecord kafkaMessageRecord = getKafkaMessageRecord(proceedingJoinPoint);

        if (kafkaRetryService.canExecuteRetry(kafkaMessageRecord)) {
            try {
                proceedingJoinPoint.proceed();
            } catch (Throwable throwable) {
                kafkaRetryService.handleFailedRetry(kafkaMessageRecord, throwable);
            }
        }
        else {
            slowLaneKafkaMessageRecordProducer.submit(kafkaMessageRecord);
        }
    }

    private KafkaMessageRecord getKafkaMessageRecord(JoinPoint joinPoint) {
        return ((ConsumerRecord<RequestCorrelationId, KafkaMessageRecord>) joinPoint.getArgs()[0]).value();
    }

}

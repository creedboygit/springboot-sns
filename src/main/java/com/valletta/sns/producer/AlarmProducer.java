package com.valletta.sns.producer;

import com.valletta.sns.model.event.AlarmEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmProducer {

    private final KafkaTemplate<Integer, AlarmEvent> kafkaTemplate;

//    private static final String TOPIC_NAME = "alarm";
    @Value("${spring.kafka.topic.alarm}")
    private String topic;

    public void send(AlarmEvent alarmEvent) {
        kafkaTemplate.send(topic, alarmEvent.getReceiveUserId(), alarmEvent);
        log.info("Send to Kafka finished. {} {}", alarmEvent.getReceiveUserId(), alarmEvent);
    }
}

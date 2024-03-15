package com.mcnc.assetmgmt.email.util.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;
/**
 * title : KafkaProducerConfig
 *
 * description : Kafka 기본설정
 *
 * reference :
 *       class com.fasterxml.jackson.databind.ser.std.StringSerializer is not an instance of org.apache.kafka.common.serialization.Serializer
 *       해당 오류 => https://stackoverflow.com/questions/53801565/common-kafkaexception-com-fasterxml-jackson-databind-ser-std-stringserializer-i
 *
 *
 *
 * author : 임현영
 * date : 2024.03.06
 **/
@Configuration
public class KafkaConfig {
    private String KAFKA_SERVER_IP;
    private String GROUP_ID;

    public KafkaConfig(@Value("${kafka.server.ip}") String KAFKA_SERVER_IP,
                       @Value("${kafka.group.id}") String GROUP_ID){
        this.KAFKA_SERVER_IP = KAFKA_SERVER_IP;
        this.GROUP_ID = GROUP_ID;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER_IP);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
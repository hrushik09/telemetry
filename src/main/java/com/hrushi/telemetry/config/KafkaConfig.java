package com.hrushi.telemetry.config;

import com.hrushi.telemetry.events.EventIngested;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import java.util.Map;

@Configuration
class KafkaConfig {
    private final String bootstrapServers;

    KafkaConfig(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    @Bean
    ProducerFactory<String, EventIngested> eventIngestedProducerFactory() {
        Map<String, Object> configs = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class,
                ProducerConfig.ACKS_CONFIG, "all",
                ProducerConfig.RETRIES_CONFIG, 3,
                ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true,
                JacksonJsonSerializer.ADD_TYPE_INFO_HEADERS, false
        );
        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    KafkaTemplate<String, EventIngested> eventIngestedKafkaTemplate(ProducerFactory<String, EventIngested> eventIngestedProducerFactory) {
        return new KafkaTemplate<>(eventIngestedProducerFactory);
    }

    @Bean
    ConsumerFactory<String, EventIngested> eventIngestedConsumerFactory() {
        Map<String, Object> configs = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false,
                JacksonJsonDeserializer.TRUSTED_PACKAGES, "com.hrushi.telemetry.events",
                JacksonJsonDeserializer.VALUE_DEFAULT_TYPE, EventIngested.class,
                JacksonJsonDeserializer.USE_TYPE_INFO_HEADERS, false
        );
        return new DefaultKafkaConsumerFactory<>(configs);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, EventIngested> eventIngestedKafkaListenerContainerFactory(ConsumerFactory<String, EventIngested> eventIngestedConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, EventIngested> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(eventIngestedConsumerFactory);
        return factory;
    }
}

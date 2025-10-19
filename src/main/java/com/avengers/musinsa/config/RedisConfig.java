package com.avengers.musinsa.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {


    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {

        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(redisHost);
        redisConfig.setPort(redisPort);

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(3))
                .build();

        return new LettuceConnectionFactory(redisConfig, clientConfig);
    }

    /**
     * 직렬화, 역직렬화를 위한 코드
     * */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {

        // Redis 템플릿 생성
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // Redis 템플릿 연결 팩토리 생성(application.yml 파일의 Redis 설정 시용)
        template.setConnectionFactory(connectionFactory);

        // ========== ObjectMapper 설정 (LocalDateTime 처리) ==========
        // Jackson ObjectMapper: Java 객체를 JSON으로 변환하는 라이브러리
        ObjectMapper objectMapper = new ObjectMapper();
        // 시간타입을 JSON 으로 직렬화 할 수 있도록 모듈 등록
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Serializer 설정
        // Redis는 바이트 배열로 데이터를 저장하므로, Java 객체 ↔ 바이트 변환 규칙 필요
        StringRedisSerializer keySerializer = new StringRedisSerializer();

        // 2. Value Serializer: Redis 값을 JSON으로 직렬화
        //  PopularKeywordResponseDTO 객체 → JSON 문자열 → 바이트 배열
        GenericJackson2JsonRedisSerializer valueSerializer =
                new GenericJackson2JsonRedisSerializer(objectMapper);

        template.setKeySerializer(keySerializer);
        template.setValueSerializer(valueSerializer);
        template.setHashKeySerializer(keySerializer);
        template.setHashValueSerializer(valueSerializer);

        return template;
    }
}
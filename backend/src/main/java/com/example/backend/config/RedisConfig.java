package com.example.backend.config;

import com.example.backend.service.MatchState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, MatchState> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, MatchState> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key as String
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Value as JSON using Jackson
        Jackson2JsonRedisSerializer<MatchState> valueSerializer = new Jackson2JsonRedisSerializer<>(MatchState.class);
        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);

        template.afterPropertiesSet();
        return template;
    }
}

package com.example.backend.config;

import com.example.backend.model.MatchStateModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, MatchStateModel> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, MatchStateModel> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key as String
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Value as JSON using Jackson
        Jackson2JsonRedisSerializer<MatchStateModel> valueSerializer = new Jackson2JsonRedisSerializer<>(MatchStateModel.class);
        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);

        template.afterPropertiesSet();
        return template;
    }
}

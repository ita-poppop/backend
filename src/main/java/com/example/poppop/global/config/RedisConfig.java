package com.example.poppop.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableRedisRepositories //RedisRepository 사용 시 필요
public class RedisConfig {
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 직렬화 설정
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());

        return template;
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // 기본 캐시 설정 (예: 만료 없음)
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig();

        // plannedPopups 캐시만 24시간 TTL 적용
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put(
                "plannedPopups", // 캐시 이름
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(24))
        );

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig) // 기본 설정(만료 없음)
                .withInitialCacheConfigurations(cacheConfigurations) // 개별 캐시 설정
                .build();
    }
}

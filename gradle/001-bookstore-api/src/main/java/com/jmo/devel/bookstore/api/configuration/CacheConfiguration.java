package com.jmo.devel.bookstore.api.configuration;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfiguration {

    public static final String AUTHORS_CACHE    = "authorsCache";
    public static final String BOOKS_CACHE      = "booksCache";
    public static final String PUBLISHERS_CACHE = "publishersCache";

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl( Duration.ofMinutes(60) )
            .disableCachingNullValues()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new GenericJackson2JsonRedisSerializer()
                )
            );
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return builder -> builder
            .withCacheConfiguration( AUTHORS_CACHE,
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(
                    Duration.ofMinutes( 10 )
                )
            )
            .withCacheConfiguration( BOOKS_CACHE,
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(
                    Duration.ofMinutes( 5 )
                )
            )
            .withCacheConfiguration( PUBLISHERS_CACHE,
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(
                    Duration.ofMinutes( 30 )
                )
            );
    }

}

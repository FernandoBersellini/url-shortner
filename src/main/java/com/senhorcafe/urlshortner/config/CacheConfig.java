package com.senhorcafe.urlshortner.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration   // registers the @Bean methods below
@EnableCaching   // turns on processing of @Cacheable / @CacheEvict / @CachePut
public class CacheConfig {

    /** Name of the shortCode -> redirect cache; referenced by the caching annotations. */
    public static final String URL_MAPPINGS = "urlMappings";

    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(60, TimeUnit.MINUTES)
                .maximumSize(10_000);
    }

    @Bean
    public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
        // Passing the name restricts the manager to this single, known cache.
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(URL_MAPPINGS);
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }
}

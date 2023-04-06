package com.ue.urlshortener.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableCaching
@Slf4j
public class CachingConfig {

    private static final String TI_CACHE = "targetIdentifiers";
    private static final int TTL = 10 * 60 * 1000;


    // An in memory cache that serves as a proof of concept
    // This is not a cloud native approach as caches will be different and confined to a single instance.
    // Current concept implementation will have higher cache-miss compared to a distributed caching solution
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(TI_CACHE);
    }

    // This will clear the cache pool entirely
    // A better eviction policy would be implemented for prod (using ehcache, for example)
    // A cache that holds onto last N request responses for example
    @CacheEvict(allEntries = true, value = {TI_CACHE})
    @Scheduled(fixedDelay = TTL, initialDelay = TTL)
    public void reportCacheEvict() {
        log.info("Cache flushed");
    }
}

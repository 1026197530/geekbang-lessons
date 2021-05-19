package org.geektimes.security.config;

import org.geektimes.security.cache.RedisCacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheManagerConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Bean(name = "redisCacheManager")
    public CacheManager cacheManager() {
        return new RedisCacheManager(host, port);
    }

}

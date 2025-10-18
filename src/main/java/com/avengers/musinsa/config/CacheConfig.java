package com.avengers.musinsa.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.parameters.P;

import java.security.PublicKey;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager BrandAndCategoriescacheManager(){
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("brands","categories");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.DAYS)
                .maximumSize(1000)
        );

        return cacheManager;
    }

}

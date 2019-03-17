package com.flatter.server.clusteringmicroservice.config;

import com.flatter.server.clusteringmicroservice.core.domain.Questionnaireable;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class CacheConfig {

    @Bean
    @Lazy
    public Cache<String, Questionnaireable> createCache() {
        return CacheBuilder.newBuilder()
                .maximumSize(1000)
                .build();
    }

    @Bean
    @Lazy
    public Cache<String, CentroidCluster<Questionnaireable>> createClusterCache() {
        return CacheBuilder.newBuilder()
                .maximumSize(1000)
                .build();
    }

    @Bean
    @Lazy
    public Cache<Questionnaireable, CentroidCluster<Questionnaireable>> createQuestionableToClusterCache() {
        return CacheBuilder.newBuilder()
                .maximumSize(1000)
                .build();
    }
}

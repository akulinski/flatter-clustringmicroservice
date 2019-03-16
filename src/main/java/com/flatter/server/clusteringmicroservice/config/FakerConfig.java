package com.flatter.server.clusteringmicroservice.config;

import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class FakerConfig {

    @Bean
    @Lazy
    public Faker provideFaker(){
        return new Faker();
    }
}

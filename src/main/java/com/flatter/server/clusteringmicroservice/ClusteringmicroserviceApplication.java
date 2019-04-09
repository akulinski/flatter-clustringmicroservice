package com.flatter.server.clusteringmicroservice;

import com.flatter.server.clusteringmicroservice.config.PropertiesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableConfigurationProperties(PropertiesConfig.class)
@EnableAsync
@EnableEurekaClient
public class ClusteringmicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClusteringmicroserviceApplication.class, args);
    }

}

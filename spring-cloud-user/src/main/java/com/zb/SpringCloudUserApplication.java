package com.zb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

// 有eureka的依赖，会自动启动eureka
//@EnableDiscoveryClient
@SpringBootApplication
public class SpringCloudUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudUserApplication.class, args);
    }

}

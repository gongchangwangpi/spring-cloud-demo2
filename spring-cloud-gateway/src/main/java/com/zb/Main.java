package com.zb;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhangbo
 * @date 2019-10-16
 */
@Slf4j
public class Main {

    public static void main(String[] args) {

        Mono<String> mono = Mono.just("123");
        System.out.println("---" + mono.log().toString());


        WebClient webClient = WebClient.builder().baseUrl("http://172.16.2.182:17087/api/mg/devices/map/1111").build();
        ClientResponse response = webClient.get().exchange().doFirst(() -> {
            log.info("response log ---");
        }).block();

        System.out.println(response.statusCode());
        response.body((resp, ctx) -> {
            resp.getBody().doOnEach((data) -> {
                InputStream inputStream = data.get().asInputStream();
                try {
                    String s = IOUtils.toString(inputStream);
                    System.out.println("response --->>> " + s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return null;
        });

    }

}

package com.zb.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zb.dto.UserDTO;
import com.zb.entity.UserEntity;
import com.zb.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author zhangbo
 * @date 2019-10-17
 */
@Slf4j
@Configuration
public class GlobalFilterConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    @Order(-1)
    public GlobalFilter loginUser() {
//            return (exchange, chain) -> exchange.getPrincipal()
//                    .map(Principal::getName)
//                    .defaultIfEmpty("Default User")
//                    .map(userName -> {
//                        //adds header to proxied request
//                        exchange.getRequest().mutate().header("CUSTOM-REQUEST-HEADER", userName).build();
//                        return exchange;
//                    })
//                    .flatMap(chain::filter);

        return (exchange, chain) -> {
            log.info(" ----- login pre filter ----- ");
            UserEntity userDTO = userService.get(1);
            String userJsonString = null;
            try {
                userJsonString = objectMapper.writeValueAsString(userDTO);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            exchange.getRequest().mutate().header("loginUser", userJsonString).build();
            return chain.filter(exchange);
        };
    }

}

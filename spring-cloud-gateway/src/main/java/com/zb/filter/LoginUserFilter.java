package com.zb.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zb.entity.UserEntity;
import com.zb.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 *
 *
 * @author zhangbo
 * @date 2019-11-06
 */
@Slf4j
@Component
public class LoginUserFilter implements GlobalFilter, Ordered {

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
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
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}

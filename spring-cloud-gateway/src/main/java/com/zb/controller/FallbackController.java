package com.zb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.factory.HystrixGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import java.util.HashMap;
import java.util.Map;

/**
 * hystrix fallback
 * 在application.yml配置{@link HystrixGatewayFilterFactory} fallbackUri
 *
 * @author zhangbo
 * @date 2019-11-05
 */
@Slf4j
@RestController
public class FallbackController {

    @RequestMapping(value = "/fallback")
    public Object fallback(ServerWebExchange exchange) {
        Object originalUrls = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        log.error("网关执行请求: {}失败, hystrix服务降级处理", originalUrls);

        Map<String, Object> map = new HashMap<>();
        map.put("code", HttpStatus.SERVICE_UNAVAILABLE.value());
        map.put("msg", HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase());
        return map;
    }

}

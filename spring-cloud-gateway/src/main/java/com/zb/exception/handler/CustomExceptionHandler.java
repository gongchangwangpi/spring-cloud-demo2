package com.zb.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zb.exception.AuthorizationException;
import com.zb.exception.BadRequestException;
import com.zb.i18n.CustomMessageSource;
import io.netty.channel.ConnectTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author zhangbo
 * @date 2019-11-01
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class CustomExceptionHandler implements ErrorWebExceptionHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomMessageSource customMessageSource;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("gateway exception, request path: {}", exchange.getRequest().getURI().getPath(), ex);

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        String msg = httpStatus.getReasonPhrase();

        if (ex instanceof AuthorizationException) {
            httpStatus = HttpStatus.UNAUTHORIZED;
            msg = customMessageSource.getMessage(exchange, ((AuthorizationException) ex).getInternationalizationField());
        }
        else if (ex instanceof BadRequestException) {
            httpStatus = HttpStatus.BAD_REQUEST;
            msg = customMessageSource.getMessage(exchange, ((BadRequestException) ex).getInternationalizationField());
        }
        else if (ex instanceof IllegalArgumentException) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            msg = customMessageSource.getMessage(exchange, ex.getMessage());
        }
        else if (ex instanceof NullPointerException) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            String message = ex.getMessage();
            if (StringUtils.isEmpty(message)) {
                msg = httpStatus.getReasonPhrase() + ". NPE";
            } else {
                msg = customMessageSource.getMessage(exchange, message);
            }
        }
        else if (ex instanceof ResponseStatusException) {
            ResponseStatusException e = (ResponseStatusException) ex;
            httpStatus = e.getStatus();
            msg = customMessageSource.getMessage(exchange, ex.getMessage());
        }
        else if (ex instanceof ConnectTimeoutException) {
            httpStatus = HttpStatus.GATEWAY_TIMEOUT;
            msg = httpStatus.getReasonPhrase();
        }
        else if (ex instanceof ConnectException) {
            httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
            msg = httpStatus.getReasonPhrase();
        }
        else {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            msg = httpStatus.getReasonPhrase();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("code", httpStatus.value());
        map.put("msg", msg);

        byte[] bytes = new byte[0];
        try {
            bytes = objectMapper.writeValueAsBytes(map);
        } catch (JsonProcessingException e) {
            //
            log.error("jackson write bytes error", e);
        }

        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatusCode(httpStatus);
        DataBuffer dataBuffer = response.bufferFactory().wrap(bytes);

        return response.writeWith(Mono.just(dataBuffer));
    }
}

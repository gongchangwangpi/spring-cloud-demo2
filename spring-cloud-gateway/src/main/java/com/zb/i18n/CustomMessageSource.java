package com.zb.i18n;

import com.zb.utils.LocaleParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Locale;

/**
 * 自定义国际化MessageSource
 *
 * @author zhangbo
 */
@Slf4j
@Component
public class CustomMessageSource {

    @Autowired
    private MessageSource messageSource;

    public String getMessage(ServerWebExchange exchange, InternationalizationField code){
        Locale locale = LocaleParser.parse(exchange);
        try {
            return messageSource.getMessage(code.getCode(), null, locale);
        } catch (NoSuchMessageException e) {
            log.warn("没有配置对应的国际化提示: {}", e.getMessage());
            return code.getDefaultMsg();
        }
    }

    public String getMessage(ServerWebExchange exchange, String msg) {
        Locale locale = LocaleParser.parse(exchange);
        try {
            return messageSource.getMessage(msg, null, locale);
        } catch (NoSuchMessageException e) {
            log.warn("没有配置对应的国际化提示: {}", e.getMessage());
        }
        return msg;
    }
}

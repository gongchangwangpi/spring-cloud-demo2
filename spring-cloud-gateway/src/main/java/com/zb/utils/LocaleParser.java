package com.zb.utils;

import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.Locale;

/**
 * @author zhangbo
 * @date 2019-11-06
 */
public class LocaleParser {

    private static final String ACCEPT_LANGUAGE_HEADER_NAME = "Accept-Language";
    private static final String LOCALE_ATTR_NAME = "REQUEST_CONTEXT_LOCALE";

    public static Locale parse(ServerWebExchange exchange) {
        Locale locale = exchange.getAttribute(LOCALE_ATTR_NAME);
        if (locale != null) {
            return locale;
        }
        List<String> headers = exchange.getRequest().getHeaders().get(ACCEPT_LANGUAGE_HEADER_NAME);
        if (!CollectionUtils.isEmpty(headers)) {
            locale = Locale.forLanguageTag(headers.stream().findFirst().get());
        }
        locale = locale == null ? Locale.getDefault() : locale;
        exchange.getAttributes().put(LOCALE_ATTR_NAME, locale);
        return locale;
    }

}

package com.zb.i18n;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用国际化字段类
 *
 * @author zhangbo
 *
 */
@Getter
@AllArgsConstructor
public enum AuthorizationI18nField implements InternationalizationField {

    AUTHORIZATION_HEADER_CANNOT_BLANK("authorization.header.cannot.blank","头部权限不能为空"),
    INVALID_AUTHORIZATION_HEADER_SIZE("invalid.authorization.header.size","无效的授权头大小"),
    INVALID_TOKEN("invalid.token","非法token"),
    TOKEN_HAS_EXPIRED("token.has.expired","token已经过期"),
    HAVE_NO_PERMISSION("have.no.permission", "您没有相关门店权限"),
    NO_API_RESOURCE_AUTH("no.api.auth", "您没有权限访问该链接");


    private String code;

    private String defaultMsg;

}

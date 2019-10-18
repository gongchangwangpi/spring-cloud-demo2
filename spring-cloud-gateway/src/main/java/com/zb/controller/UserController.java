package com.zb.controller;

import com.zb.entity.UserEntity;
import com.zb.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhangbo
 * @date 2019-10-18
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/auth/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 不能使用 HttpServletRequest
    @GetMapping(value = "/{id}")
    public UserEntity get(@PathVariable("id") Integer id, ServerWebExchange exchange/*, HttpServletRequest request*/) {
        log.info("ServerWebExchange path == {}", exchange.getRequest().getPath());
//        log.info("HttpServletRequest requestURI == {}", request.getRequestURI());
        return userService.get(id);
    }

}

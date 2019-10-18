package com.zb.controller;

import com.zb.dto.ResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangbo
 * @date 2019-10-16
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    @Value("${spring.profiles:default}")
    private String profiles;

    @GetMapping
    public ResultDTO list(@RequestHeader("loginUser") String loginUser) {
        log.info("request header loginUser: {}", loginUser);
        return ResultDTO.success(profiles);
    }

}

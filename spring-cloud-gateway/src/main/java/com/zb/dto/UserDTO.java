package com.zb.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangbo
 * @date 2019-10-17
 */
@Data
public class UserDTO implements Serializable {

    private Integer id;

    private Integer companyId;

    private String mobile;

}

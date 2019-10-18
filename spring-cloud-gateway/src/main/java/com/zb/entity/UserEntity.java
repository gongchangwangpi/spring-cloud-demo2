package com.zb.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhangbo
 * @date 2019-10-18
 */
@Data
@TableName("a_user")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = -288804775088354048L;

    /**
     * 主键
     */
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 是否启用
     */
    private Boolean enabled;
    /**
     * 密码
     */
    private String password;
    /**
     * 到期时间
     */
    private Date createTime;

}

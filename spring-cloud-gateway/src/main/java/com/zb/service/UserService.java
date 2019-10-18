package com.zb.service;

import com.zb.dao.UserMapper;
import com.zb.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhangbo
 * @date 2019-10-17
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userDao;

    public UserEntity get(Integer id) {
        return userDao.selectById(id);
    }

}

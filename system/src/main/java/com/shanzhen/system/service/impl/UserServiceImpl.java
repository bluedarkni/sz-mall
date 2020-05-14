package com.shanzhen.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shanzhen.system.dao.UserDao;
import com.shanzhen.system.model.dto.UserDTO;
import com.shanzhen.system.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description:
 * Created by nijunyang on 2020/5/12 15:01
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO findByName(String userName) {
        String encode = passwordEncoder.encode("123456");
        LambdaQueryWrapper<UserDTO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDTO::getUsername, userName);
        return userDao.selectOne(queryWrapper);
    }

    @Override
    public Object create(UserDTO user) {
        String encodePwd = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePwd);
        int insert = userDao.insert(user);
        return insert;
    }
}

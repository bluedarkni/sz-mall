package com.shanzhen.system.controller;

import com.shanzhen.system.model.dto.UserDTO;
import com.shanzhen.system.service.UserService;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Description: 用户操作
 * Created by nijunyang on 2020/5/14 16:21
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    /**
     * 创建用户
     *
     * @param user
     * @return
     */
    @PostMapping
    public ResponseEntity<Object> create(@Validated @RequestBody UserDTO user) {
        Object object = userService.create(user);
        return new ResponseEntity<>(object, HttpStatus.CREATED);
    }
}

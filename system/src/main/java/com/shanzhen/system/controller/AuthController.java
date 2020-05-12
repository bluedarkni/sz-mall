package com.shanzhen.system.controller;

import com.shanzhen.system.model.vo.AuthUser;
import com.shanzhen.system.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Description:
 * Created by nijunyang on 2020/5/12 15:05
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    public ResponseEntity<Object> login(@Validated @RequestBody AuthUser authUser, HttpServletRequest request){
        Object object = authService.login(authUser);
        return ResponseEntity.ok(object);
    }

}

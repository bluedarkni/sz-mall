package com.shanzhen.system.service;

import com.shanzhen.system.model.vo.AuthUser;

/**
 * Description:
 * Created by nijunyang on 2020/5/12 15:11
 */
public interface AuthService {

    Object login(AuthUser authUser);
}
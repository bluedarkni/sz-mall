package com.shanzhen.system.service;

import com.shanzhen.system.model.dto.UserDTO;

/**
 * Description:
 * Created by nijunyang on 2020/5/12 15:01
 */
public interface UserService {

    UserDTO findByName(String userName);

    Object create(UserDTO user);
}

package com.shanzhen.system.service.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shanzhen.common.exception.ErrorCodeException;
import com.shanzhen.system.error.SystemErrorCode;
import com.shanzhen.system.model.dto.UserDTO;
import com.shanzhen.system.model.vo.auth.JwtUser;
import com.shanzhen.system.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Description:
 * Created by nijunyang on 2020/5/14 13:39
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userDTO = userService.findByName(username);
        if (userDTO == null) {
            throw new ErrorCodeException(SystemErrorCode.USER_NOT_EXITS_1003);
        }
        return createJwtUser(userDTO);

    }

    private UserDetails createJwtUser(UserDTO userDTO) {

        return new JwtUser(
                userDTO.getId(),
                userDTO.getUsername(),
                userDTO.getNickName(),
                userDTO.getPassword(),
                userDTO.getEmail(),
                userDTO.getPhone(),
                new ArrayList<>(),
                userDTO.getEnabled());
    }
}

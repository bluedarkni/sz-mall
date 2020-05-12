package com.shanzhen.system.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shanzhen.common.entity.BaseEntity;
import lombok.Data;


/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
@Data
public class UserDTO extends BaseEntity {

    private String username;

    @JsonIgnore
    private String password;

    private String nickName;

    private String email;

    private String phone;

}

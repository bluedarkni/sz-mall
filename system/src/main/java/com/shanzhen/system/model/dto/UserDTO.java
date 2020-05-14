package com.shanzhen.system.model.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shanzhen.common.entity.BaseEntity;
import lombok.Data;


/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
@Data
@TableName("user")
public class UserDTO extends BaseEntity {

    private String username;

    @JsonIgnore
    private String password;

    private String nickName;

    private String email;

    private String phone;

    private Boolean enabled = true;

}

package com.shanzhen.common.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shanzhen.common.utils.cipher.InstantJacksonDeserialize;
import com.shanzhen.common.utils.cipher.InstantJacksonSerializer;
import lombok.Data;

import java.time.Instant;

/**
 * Description: 基础字段
 * Created by nijunyang on 2020/5/12 14:40
 */
@Data
public class BaseEntity {

    @TableId(type = IdType.AUTO)
    protected Long id;

    @TableField(fill = FieldFill.INSERT)
    protected Long createdBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected Long updatedBy;

    @TableField(fill = FieldFill.INSERT)
    @JsonSerialize(using = InstantJacksonSerializer.class)
    @JsonDeserialize(using = InstantJacksonDeserialize.class)
    protected Instant createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonSerialize(using = InstantJacksonSerializer.class)
    @JsonDeserialize(using = InstantJacksonDeserialize.class)
    protected Instant updatedTime;

    @Version
    @TableField(fill = FieldFill.INSERT)
    protected Integer version;

    @TableLogic  //逻辑删除
    @TableField(fill = FieldFill.INSERT)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected Byte deleted;
}

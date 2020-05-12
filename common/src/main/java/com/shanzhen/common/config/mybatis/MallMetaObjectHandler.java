package com.shanzhen.common.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

/**
 * Description:
 * Created by nijunyang on 2020/5/9 17:52
 */
public class MallMetaObjectHandler implements MetaObjectHandler, InitializingBean {

    private static final String CREATED_BY = "createdBy";

    private static final String UPDATED_BY = "updatedBy";

    private static final String CREATED_TIME = "createdTime";

    private static final String UPDATED_TIME = "updatedTime";

    private static final String DELETED = "deleted";

    private static final String VERSION = "version";

    private static final String HEADER_USER = "user";

    @Autowired
    private HttpServletRequest request;

    private ObjectMapper mapper;

    @Override
    public void afterPropertiesSet() {
        mapper = Jackson2ObjectMapperBuilder.json().failOnUnknownProperties(false)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).build();
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        Instant date = Instant.now();
        setFieldValByName(CREATED_BY, getUserId(), metaObject);
        setFieldValByName(UPDATED_BY, getUserId(), metaObject);
        setFieldValByName(CREATED_TIME, date, metaObject);
        setFieldValByName(UPDATED_TIME, date, metaObject);
        setFieldValByName(DELETED, 0, metaObject);
        setFieldValByName(VERSION, 1, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long userId = getUserId();
        if (userId != null) {
            setFieldValByName(UPDATED_BY, getUserId(), metaObject);
        }
        setFieldValByName(UPDATED_TIME, Instant.now(), metaObject);
    }

    public Long getUserId() {
        try {
            String str = request.getHeader(HEADER_USER);
            if (str != null) {
                try {
//                    return mapper.readValue(URLDecoder.decode(str, "UTF-8"), UserDTO.class).getId();
                } catch (Exception e) {
                    return null;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}

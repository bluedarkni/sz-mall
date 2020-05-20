package com.shanzhen.common.exception;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Description:
 * Created by nijunyang on 2020/5/9 17:36
 */
@ControllerAdvice
@Slf4j
public class ErrorCodeExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    /**
     * 统一处理ErrorCodeException业务异常，返回前台错误提示
     *
     * @param exception exception
     * @param locale    locale
     * @return
     */
    @ExceptionHandler(value = ErrorCodeException.class)
    public final ResponseEntity<ErrorCodeResponse> handleErrorCodeException(ErrorCodeException exception, Locale locale) {
        String message = messageSource.getMessage(exception.getErrorCode().getKey(), exception.getArgs(), locale);
        ErrorCodeResponse errorCodeResponse = new ErrorCodeResponse(exception.getErrorCode().getCode(), message);
        return new ResponseEntity<>(errorCodeResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * 请求参数校验异常
     *
     * @param exception exception
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorCodeResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        String message = JSON.toJSONString(errors.values());
        return new ResponseEntity<>(new ErrorCodeResponse(message), HttpStatus.BAD_REQUEST);
    }
}

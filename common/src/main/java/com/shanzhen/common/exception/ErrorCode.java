package com.shanzhen.common.exception;

/**
 * Description:
 * Created by nijunyang on 2020/5/9 17:29
 */
public interface ErrorCode {

    /**
     * 资源文件中的key
     * @return
     */
    String getKey();

    /**
     * 获取错误码
     * @return
     */
    int getCode();
}

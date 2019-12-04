package com.kairlec.exception;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 错误码接口
 */
public interface ErrorCode {


    /**
     * 获取错误码
     *
     * @return
     */
    @JSONField(name="code")
    int getCode();

    /**
     * 获取错误信息
     *
     * @return
     */
    @JSONField(name="data")
    Object getData();

    /**
     * 判断是否是同一种错误
     * @return
     */
    boolean equals(ErrorCode other);

}

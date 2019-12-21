package com.kairlec.interfaca;


public interface ResponseDataInterface {

    /**
     * 获取错误码
     *
     * @return
     */
    int getCode();

    /**
     * 获取错误码
     *
     * @return
     */
    String getMessage();

    /**
     * 获取数据
     *
     * @return
     */
    Object getData();

    /**
     * 判断是否是同一个信息
     *
     * @return
     */
    boolean equals(ResponseDataInterface other);


}

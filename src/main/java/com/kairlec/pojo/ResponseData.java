package com.kairlec.pojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.kairlec.interfaca.ResponseDataInterface;


public class ResponseData implements ResponseDataInterface {

    @JSONField(name = "code",ordinal = 0)
    private int code;
    @JSONField(name = "msg",ordinal = 1)
    private String Message;
    @JSONField(name = "data",ordinal = 2)
    private Object object;

    public ResponseData(int code, String Message, Object object) {
        this.code = code;
        this.Message = Message;
        this.object = object;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public Object getData() {
        return object;
    }

    @Override
    public String getMessage() {
        return Message;
    }

    @Override
    public boolean equals(ResponseDataInterface responseDataInterface) {
        return this.code == responseDataInterface.getCode();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }

}

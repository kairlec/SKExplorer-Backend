package com.kairlec.pojo.Json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;


@Data
public class FileInfo {
    private String name;
    private String type;
    private long size = 0;
    private long editTime = 0;
    private String description;
    private String path;
    @JSONField(serialize = false)
    private boolean exist;

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }

}

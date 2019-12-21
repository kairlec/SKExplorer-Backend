package com.kairlec.pojo.Json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;


@Data
public class FileInfo {
    @JSONField(name = "name")
    private String name;
    @JSONField(name = "type")
    private String type;
    @JSONField(name = "size")
    private long size = 0;
    @JSONField(name = "editTime")
    private long editTime = 0;
    @JSONField(name = "description")
    private String description;
    @JSONField(name = "path")
    private String path;
    @JSONField(serialize = false)
    private boolean exist;
    @JSONField(serialize = false, name = "redirect")
    private boolean redirect;

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }

}

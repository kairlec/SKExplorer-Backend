package com.kairlec.pojo.Json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;

import java.util.List;

@Data
public class FileList {
    @JSONField(name = "isRoot")
    private boolean isRoot;

    @JSONField(name = "items")
    private List<FileInfo> items;

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }

}

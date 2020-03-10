package com.kairlec.pojo.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

data class FileInfo(
        var name: String,
        var type: String? = null,
        var size: Long = 0,
        var editTime: Long = 0,
        var description: String? = null,
        var path: String,
        @JSONField(serialize = false)
        var exist: Boolean = false
) : Comparable<FileInfo> {
    override fun compareTo(other: FileInfo): Int {
        if (this.type == null || other.type == null) {
            return this.name.compareTo(other.name)
        }
        if (this.type == "folder" && other.type != "folder") {
            return -1
        }
        if (other.type == "folder" && this.type != "folder") {
            return 1
        }
        return this.name.compareTo(other.name)
    }

    override fun toString(): String {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue)
    }
}

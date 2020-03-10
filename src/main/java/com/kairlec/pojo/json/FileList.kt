package com.kairlec.pojo.json

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.annotation.JSONField
import com.alibaba.fastjson.serializer.SerializerFeature


data class FileList(
        @JSONField(name = "root")
        var root: Boolean,
        var items: ArrayList<FileInfo>,
        @JSONField(name = "local")
        var localPath: String
) {

    fun updateItems(items: ArrayList<FileInfo>) {
        this.items = items
        items.sort()
    }

    override fun toString(): String {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue)
    }

    init {
        items.sort()
    }
}

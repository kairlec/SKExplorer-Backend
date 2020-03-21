package com.kairlec.pojo.json

import com.fasterxml.jackson.annotation.JsonProperty


data class FileList(
        var root: Boolean,
        var items: ArrayList<FileInfo>,
        @JsonProperty("local")
        var localPath: String
) {

    fun updateItems(items: ArrayList<FileInfo>) {
        this.items = items
        items.sort()
    }

    init {
        items.sort()
    }
}

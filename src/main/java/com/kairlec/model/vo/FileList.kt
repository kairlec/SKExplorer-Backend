package com.kairlec.model.vo

import com.fasterxml.jackson.annotation.JsonProperty


class FileList(val root: Boolean, items: ArrayList<FileInfo>, @JsonProperty("local") val localPath: String) {
    var items: ArrayList<FileInfo> = items
        set(value) {
            field = value
            field.sort()
        }

    init {
        items.sort()
    }

}

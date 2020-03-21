package com.kairlec.pojo.json;


data class FileInfo(
        var name: String,
        var type: String? = null,
        var size: Long = 0,
        var editTime: Long = 0,
        var path: String
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

}

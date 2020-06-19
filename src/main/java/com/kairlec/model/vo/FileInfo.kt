package com.kairlec.model.vo;

data class FileInfo(
        val name: String,
        val type: String? = null,
        val size: Long = 0,
        val editTime: Long = 0,
        val path: RelativePath,
        val extraInfo: ExtraInfo
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

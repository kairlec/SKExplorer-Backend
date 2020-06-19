package com.kairlec.model.vo

import java.util.*

data class Announcement(
        val id: String,
        var content: String,
        val publicTime: Long,
        var modifyTime: Long
) {
    companion object {
        fun newInstance(content: String, publicTime: Long, modifyTime: Long) = Announcement(UUID.randomUUID().toString(), content, publicTime, modifyTime)
    }
}
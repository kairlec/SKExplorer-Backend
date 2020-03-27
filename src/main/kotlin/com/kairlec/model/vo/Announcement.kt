package com.kairlec.model.vo

data class Announcement(
        val id: String,
        val content: String,
        val publicTime: Long,
        val modifyTime: Long
)
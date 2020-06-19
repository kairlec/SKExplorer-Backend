package com.kairlec.service

import com.kairlec.model.vo.Announcement
import org.springframework.stereotype.Service

@Service
interface AnnouncementService {
    fun getLatest(): Announcement?

    fun update(announcement: Announcement)

    fun delete(id: String)

    fun add(content: String): Announcement

    fun getAll(): ArrayList<Announcement>

    fun getAllContent(): String


}
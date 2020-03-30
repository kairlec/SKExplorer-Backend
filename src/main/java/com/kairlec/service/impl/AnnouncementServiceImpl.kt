package com.kairlec.service.impl

import com.kairlec.dao.AnnouncementDAO
import com.kairlec.model.vo.Announcement
import com.kairlec.service.AnnouncementService
import org.springframework.stereotype.Service

@Service
class AnnouncementServiceImpl : AnnouncementService {

    override fun getLatest(): Announcement? {
        return AnnouncementDAO.getAll().maxBy { it.publicTime }
    }

    override fun update(announcement: Announcement) {
        AnnouncementDAO.update(announcement)
    }

    override fun delete(id: String) {
        AnnouncementDAO.delete(id)
    }

    override fun add(content: String): Announcement {
        return AnnouncementDAO.add(content)
    }

    override fun getAll(): ArrayList<Announcement> {
        return AnnouncementDAO.getAll()
    }

    override fun getAllContent(): String {
        return AnnouncementDAO.getAllContent()
    }

}
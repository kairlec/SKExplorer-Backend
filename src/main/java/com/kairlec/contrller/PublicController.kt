package com.kairlec.contrller

import com.kairlec.annotation.JsonRequestMapping
import com.kairlec.intf.ResponseDataInterface
import com.kairlec.local.utils.ResponseDataUtils.responseOK
import com.kairlec.service.impl.AnnouncementServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@JsonRequestMapping(value = ["/public"])
@RestController
class PublicController {

    @Autowired
    private lateinit var announcementServiceImpl: AnnouncementServiceImpl

    @RequestMapping(value = ["/announcement/latest"])
    fun announcement(): ResponseDataInterface {
        return announcementServiceImpl.getLatest().responseOK
    }

    @RequestMapping(value = ["/announcement/all"])
    fun allAnnouncement(): ResponseDataInterface {
        return announcementServiceImpl.getAll().responseOK
    }



}
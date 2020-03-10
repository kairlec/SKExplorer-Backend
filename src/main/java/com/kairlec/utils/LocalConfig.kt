package com.kairlec.utils

import com.kairlec.service.impl.DescriptionMapServiceImpl
import com.kairlec.service.impl.MimeMapServiceImpl
import com.kairlec.service.impl.UserServiceImpl
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class LocalConfig {
    @Autowired
    private lateinit var descriptionMapServiceTemp: DescriptionMapServiceImpl

    @Autowired
    private lateinit var mimeMapServiceTemp: MimeMapServiceImpl

    @Autowired
    private lateinit var userServiceTemp: UserServiceImpl

    @PostConstruct
    fun beforeInit() {
        descriptionMapService = descriptionMapServiceTemp
        userService = userServiceTemp
        mimeMapService = mimeMapServiceTemp
        if (descriptionMapService.init() == null) {
            logger.warn("DescriptionMapService init result null")
        }
        if (userService.init() == null) {
            logger.warn("UserService init result null")
        }
        if (mimeMapService.init() == null) {
            logger.warn("MimeMapService init result null")
        }
    }

    companion object {
        private val logger = LogManager.getLogger(LocalConfig::class.java)
        lateinit var descriptionMapService: DescriptionMapServiceImpl
        lateinit var mimeMapService: MimeMapServiceImpl
        lateinit var userService: UserServiceImpl
    }
}

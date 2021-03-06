package com.kairlec.local.utils

import com.kairlec.constant.SKException
import com.kairlec.constant.ServiceErrorEnum
import org.apache.logging.log4j.LogManager

object ResponseDataUtils {
    private val logger = LogManager.getLogger(ResponseDataUtils::class.java)

    val Any?.responseOK
        get() = ServiceErrorEnum.NO_ERROR.data(this)

    val Exception.responseError: ServiceErrorEnum
        get() {
            if (this is SKException) {
                logger.error("a SKExplorer has throwout:${this.message}")
                this.getServiceErrorEnum()?.let {
                    logger.error("""${it.msg} with data "${it.data}"""", this)
                    return it
                }
            }
            logger.error("a Exception has throwout:${this.message}", this)
            return ServiceErrorEnum.fromException(this)
        }

}

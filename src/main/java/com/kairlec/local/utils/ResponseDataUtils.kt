package com.kairlec.local.utils

import com.kairlec.exception.SKException
import com.kairlec.exception.ServiceErrorEnum
import com.kairlec.`interface`.ResponseDataInterface
import org.apache.logging.log4j.LogManager

object ResponseDataUtils {
    private val logger = LogManager.getLogger(ResponseDataUtils::class.java)

    private fun fromException(e: Exception): ServiceErrorEnum {
        return ServiceErrorEnum.fromException(e)
    }

    fun ok(data: Any? = null): ResponseDataInterface {
        return ServiceErrorEnum.NO_ERROR.data(data)
    }

    fun error(e: Exception): ResponseDataInterface {
        e.printStackTrace()
        if(e is SKException){
            logger.error("a SKExplorer has throwout")
            e.getServiceErrorEnum()?.let{
                return error(it)
            }
        }
        logger.error("a Exception has throwout")
        return fromException(e)
    }

    fun error(serviceErrorEnum: ServiceErrorEnum): ResponseDataInterface {
        return serviceErrorEnum
    }
}

package com.kairlec.local.utils

import com.kairlec.exception.SKException
import com.kairlec.exception.ServiceErrorEnum
import org.apache.logging.log4j.LogManager

object ResponseDataUtils {
    private val logger = LogManager.getLogger(ResponseDataUtils::class.java)

    private fun fromException(e: Exception): ServiceErrorEnum {
        return ServiceErrorEnum.fromException(e)
    }

    fun ok(data: Any? = null): String {
        return ServiceErrorEnum.NO_ERROR.data(data).toString()
    }

    fun error(e: Exception): String {
        e.printStackTrace()
        if(e is SKException){
            logger.error("a SKExplorer has throwout")
            e.getServiceErrorEnum()?.let{
                return error(it)
            }
        }
        logger.error("a Explorer has throwout")
        return fromException(e).toString()
    }

    fun error(serviceErrorEnum: ServiceErrorEnum): String {
        return serviceErrorEnum.toString()
    }
}

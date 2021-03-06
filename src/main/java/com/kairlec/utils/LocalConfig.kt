package com.kairlec.utils

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.kairlec.contrller.FileController
import com.kairlec.service.impl.ConfigServiceImpl
import com.kairlec.service.impl.ExtraInfoServiceImpl
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.text.DateFormat
import javax.annotation.PostConstruct

@Component
class LocalConfig {

    @Autowired
    private lateinit var configServiceImpl: ConfigServiceImpl

    @Autowired
    private lateinit var extraInfoServiceImpl: ExtraInfoServiceImpl

    @PostConstruct
    fun init() {
        Companion.configServiceImpl = configServiceImpl
        Companion.extraInfoServiceImpl = extraInfoServiceImpl
    }

    companion object {
        lateinit var configServiceImpl: ConfigServiceImpl
        lateinit var extraInfoServiceImpl: ExtraInfoServiceImpl

        val objectMapper: ObjectMapper =
                jacksonObjectMapper()
                        .setSerializationInclusion(JsonInclude.Include.ALWAYS)
                        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                        .disable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)
                        .setDateFormat(DateFormat.getDateTimeInstance())


        fun String.Companion.toJSON(`object`: Any): String {
            return objectMapper.writeValueAsString(`object`)
        }

        inline fun <reified T> String.toObject(): T? {
            return try {
                objectMapper.readValue(this)
            } catch (e: JsonParseException) {
                null
            }
        }

        fun String.toJsonNode(): JsonNode? {
            return try {
                objectMapper.readTree(this)
            } catch (e: JsonParseException) {
                null
            }
        }

        fun String.toObjectNode(): ObjectNode? {
            return try {
                objectMapper.readTree(this) as ObjectNode
            } catch (e: JsonParseException) {
                null
            }
        }

        private val logger = LogManager.getLogger(LocalConfig::class.java)
    }
}

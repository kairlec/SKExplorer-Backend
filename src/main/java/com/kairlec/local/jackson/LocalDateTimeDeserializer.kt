package com.kairlec.local.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.time.LocalDateTime


class LocalDateTimeDeserializer : StdDeserializer<LocalDateTime>(LocalDateTime::class.java) {
    override fun deserialize(parser: JsonParser, context: DeserializationContext): LocalDateTime {
        return LocalDateTime.parse(parser.readValueAs(String::class.java))
    }
}
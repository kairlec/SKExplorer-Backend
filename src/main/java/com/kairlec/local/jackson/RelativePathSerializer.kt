package com.kairlec.local.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.kairlec.model.vo.RelativePath


class RelativePathSerializer : JsonSerializer<RelativePath>() {
    override fun serialize(value: RelativePath, jgen: JsonGenerator, provider: SerializerProvider) {
        jgen.writeString(value.path)
    }
}

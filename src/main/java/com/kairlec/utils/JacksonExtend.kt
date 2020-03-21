package com.kairlec.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode

/**
 *@program: Backend
 *@description: Jackson增强
 *@author: Kairlec
 *@create: 2020-03-14 19:32
 */


fun JsonNode?.asLongOrNull(): Long? {
    if (this == null || !this.isLong) {
        return null
    }
    return this.asLong()
}

fun JsonNode?.asDoubleOrNull(): Double? {
    if (this == null || !this.isDouble) {
        return null
    }
    return this.asDouble()
}

fun JsonNode?.asTextOrNull(): String? {
    if (this == null || !this.isTextual) {
        return null
    }
    return this.asText()
}

fun JsonNode?.asIntOrNull(): Int? {
    if (this == null || !this.isInt) {
        return null
    }
    return this.asInt()
}

fun JsonNode?.asBooleanOrNull(): Boolean? {
    if (this == null || !this.isBoolean) {
        return null
    }
    return this.asBoolean()
}

fun ObjectNode?.asLongOrNull(): Long? {
    if (this == null || !this.isLong) {
        return null
    }
    return this.asLong()
}

fun ObjectNode?.asDoubleOrNull(): Double? {
    if (this == null || !this.isDouble) {
        return null
    }
    return this.asDouble()
}

fun ObjectNode?.asTextOrNull(): String? {
    if (this == null || !this.isTextual) {
        return null
    }
    return this.asText()
}

fun ObjectNode?.asIntOrNull(): Int? {
    if (this == null || !this.isInt) {
        return null
    }
    return this.asInt()
}

fun ObjectNode?.asBooleanOrNull(): Boolean? {
    if (this == null || !this.isBoolean) {
        return null
    }
    return this.asBoolean()
}

operator fun ObjectNode.set(fieldName: String, data: Any?) {
    this.putPOJO(fieldName, data)
}
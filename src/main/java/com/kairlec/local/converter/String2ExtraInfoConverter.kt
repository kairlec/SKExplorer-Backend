package com.kairlec.local.converter

import com.kairlec.model.vo.ExtraInfo
import com.kairlec.utils.LocalConfig.Companion.toObject
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component


@Component
class String2ExtraInfoConverter : Converter<String, ExtraInfo> {
    override fun convert(extraInfoString: String): ExtraInfo {
        return extraInfoString.toObject<ExtraInfo>() ?: throw IllegalArgumentException("不是标准的ExtraInfo类JSON")
    }
}
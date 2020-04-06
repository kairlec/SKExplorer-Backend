package com.kairlec.local.converter

import com.kairlec.model.vo.RelativePath
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component


@Component
class String2RelativePathConverter : Converter<String, RelativePath> {
    override fun convert(source: String): RelativePath {
        return RelativePath(source)
    }
}
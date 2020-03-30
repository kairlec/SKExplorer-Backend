package com.kairlec.service

import com.kairlec.model.bo.AbsolutePath
import com.kairlec.model.vo.ExtraInfo
import com.kairlec.model.vo.RelativePath
import org.springframework.stereotype.Service

@Service
interface ExtraInfoService {
    fun getExtraInfo(path: RelativePath): ExtraInfo?

    fun setExtraInfo(path: RelativePath, extraInfo: ExtraInfo)

    fun getExtraInfoDefault(path: RelativePath): ExtraInfo
}
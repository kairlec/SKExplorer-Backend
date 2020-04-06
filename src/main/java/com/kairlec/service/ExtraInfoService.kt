package com.kairlec.service

import com.kairlec.model.vo.ExtraInfo
import com.kairlec.model.vo.RelativePath
import org.springframework.stereotype.Service

@Service
interface ExtraInfoService {
    fun getExtraInfo(path: RelativePath): ExtraInfo?

    fun setExtraInfo(path: RelativePath, extraInfo: ExtraInfo)

    fun getExtraInfoDefault(path: RelativePath): ExtraInfo

    fun moveExtraInfo(path: RelativePath, target: RelativePath, isReplace: Boolean)

    fun renameExtraInfo(path: RelativePath, name: String)

    fun deleteExtraInfo(path: RelativePath)
}
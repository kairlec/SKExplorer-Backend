package com.kairlec.service.impl

import com.kairlec.dao.ExtraInfoDAO
import com.kairlec.model.vo.ExtraInfo
import com.kairlec.model.vo.RelativePath
import com.kairlec.service.ExtraInfoService
import org.springframework.stereotype.Service

@Service
class ExtraInfoServiceImpl : ExtraInfoService {
    override fun getExtraInfo(path: RelativePath): ExtraInfo? {
        return ExtraInfoDAO.getExtraInfo(path)
    }

    override fun setExtraInfo(path: RelativePath, extraInfo: ExtraInfo) {
        ExtraInfoDAO.updateOrCreateExtraInfo(path, extraInfo)
    }

    override fun getExtraInfoDefault(path: RelativePath): ExtraInfo {
        return ExtraInfoDAO.getExtraInfo(path) ?: ExtraInfo.Default
    }

    override fun moveExtraInfo(path: RelativePath, target: RelativePath, isReplace: Boolean) {
        return ExtraInfoDAO.moveExtraInfo(path, target, isReplace)
    }

    override fun renameExtraInfo(path: RelativePath, name: String) {
        return ExtraInfoDAO.renameExtraInfo(path, name)
    }


    override fun deleteExtraInfo(path: RelativePath) {
        ExtraInfoDAO.deleteExtraInfo(path)
    }
}
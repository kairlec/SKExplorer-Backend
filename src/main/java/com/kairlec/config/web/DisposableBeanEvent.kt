package com.kairlec.config.web

import com.kairlec.config.editable.EditableConfig
import org.springframework.beans.factory.DisposableBean
import org.springframework.stereotype.Component

/**
 * @program: SKExplorer
 * @description: 程序退出时保存配置
 * @author: Kairlec
 * @create: 2020-03-17 12:01
 */

@Component
class DisposableBeanEvent : DisposableBean {
    override fun destroy() {
        EditableConfig.save()
    }
}
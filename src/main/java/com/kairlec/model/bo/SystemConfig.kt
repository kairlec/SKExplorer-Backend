package com.kairlec.model.bo

/**
 * @program: SKExplorer
 * @description: 系统配置
 * @author: Kairlec
 * @create: 2020-03-16 19:26
 */

data class SystemConfig(
        var redirectEnable: Boolean
) {

    fun updateTo(systemConfig: SystemConfig) {
        this.redirectEnable = systemConfig.redirectEnable
    }

    companion object {
        val Default
            get() = SystemConfig(true)
    }
}
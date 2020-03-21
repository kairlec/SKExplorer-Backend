package com.kairlec.pojo

/**
 * @program: SKExplorer
 * @description: 系统配置
 * @author: Kairlec
 * @create: 2020-03-16 19:26
 */

data class SystemConfig(
        var redirectEnable: Boolean
) {
    companion object {
        val Default
            get() = SystemConfig(true)
    }
}
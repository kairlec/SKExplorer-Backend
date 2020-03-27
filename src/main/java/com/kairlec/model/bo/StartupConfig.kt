package com.kairlec.model.bo

import java.nio.file.Path

/**
 * @program: SKExplorer
 * @description: 程序启动配置
 * @author: Kairlec
 * @create: 2020-03-08 18:07
 * @suppress
 */

data class StartupConfig(
        val contentDir: String,
        val excludeDir: Array<String>,
        val excludeFile: Array<String>,
        val excludeExt: Array<String>,
        val captchaCount: Int = 4,
        val dataDirPath: Path
)

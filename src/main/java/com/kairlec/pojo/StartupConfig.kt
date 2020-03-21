package com.kairlec.pojo

import com.kairlec.dao.ConfigDao
import org.apache.logging.log4j.CloseableThreadContext
import org.apache.logging.log4j.LogManager
import java.nio.file.Path

/**
 * @program: SKExplorer
 * @description: 程序启动配置
 * @author: Kairlec
 * @create: 2020-03-08 18:07
 * @suppress
 */

data class StartupConfig(
        var contentDir: String,
        var excludeDir: Array<String>,
        var excludeFile: Array<String>,
        var excludeExt: Array<String>,
        var captchaCount: Int = 4,
        var configFilePath: Path
)

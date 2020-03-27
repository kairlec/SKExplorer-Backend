package com.kairlec.utils

import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.net.URLDecoder
import java.util.*
import kotlin.collections.ArrayList

object ClassUtils {
    private val logger = LogManager.getLogger(ClassUtils::class.java)
    private const val EXT = "class"

    /**
     * 根据包名获取包的URL
     * @param pkgName
     * @return
     */
    fun getPkgPath(pkgName: String): String {
        val pkgDirName = pkgName.replace('.', File.separatorChar)
        val url = this::class.java.classLoader.getResource(pkgDirName) ?: throw Exception("指定的包名不存在:${pkgName}")
        return URLDecoder.decode(url.file, Charsets.UTF_8)
    }

    /**
     * 获取指定包下所有类对象的集合
     * @param pkgName 包名(com.demo.controller)
     * @param pkgPath 包路径(/Users/xxx/workspace/java/project/out/production/classes/com/demo/controller)
     * @param recursive 是否递归遍历子目录
     * @return 类集合
     */
    fun scanClasses(pkgName: String, pkgPath: String = getPkgPath(pkgName), recursive: Boolean = true): Set<Class<*>> {
        val classesSet: MutableSet<Class<*>> = HashSet()
        val allClassFile = getAllClassFile(pkgPath, recursive)
        for (curFile in allClassFile) {
            try {
                classesSet.add(getClassObj(curFile, pkgName, pkgPath))
            } catch (e: ClassNotFoundException) {
                logger.error("load class fail", e)
            }
        }
        return classesSet
    }


    /**
     * 加载类
     * @param file
     * @param pkgPath
     * @param pkgName
     * @return
     */
    private fun getClassObj(file: File, pkgName: String, pkgPath: String = getPkgPath(pkgName)): Class<*> {
        // 考虑class文件在子目录中的情况
        val absPath = file.absolutePath.substring(0, file.absolutePath.length - EXT.length - 1)
        var className = absPath.substring(pkgPath.length).replace(File.separatorChar, '.')
        className = if (className.startsWith(".")) pkgName + className else "$pkgName.$className"
        return this::class.java.classLoader.loadClass(className)
    }

    /**
     * 遍历指定目录下所有扩展名为class的文件
     * @param pkgPath 包目录
     * @param recursive 是否递归遍历子目录
     * @return
     */
    private fun getAllClassFile(pkgPath: String, recursive: Boolean = true): Collection<File> {
        val fPkgDir = File(pkgPath)
        if (!(fPkgDir.exists() && fPkgDir.isDirectory)) {
            logger.info("the directory to package is empty: {}", fPkgDir.absolutePath)
            return emptyList()
        }
        val list = ArrayList<File>()
        fPkgDir.walk()
                .maxDepth(if (recursive) Int.MAX_VALUE else 1)
                .onEnter { true }
                .onLeave { }
                .onFail { file: File, e: IOException -> run { logger.error("""walk to file "$file" throwed a exception:${e.message}""", e) } }
                .filter { it.isFile && it.extension == "class" }
                .forEach {
                    list.add(it)
                }
        return list
    }

}
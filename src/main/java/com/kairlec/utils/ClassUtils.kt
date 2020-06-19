package com.kairlec.utils

import org.apache.logging.log4j.LogManager
import java.io.File
import java.net.JarURLConnection
import java.util.*
import java.util.jar.JarFile

object ClassUtils {
    private val logger = LogManager.getLogger(ClassUtils::class.java)
    private const val CLASS_SUFFIX = ".class"
    private val CLASS_FILE_PREFIX = File.separator + "classes" + File.separator
    private const val PACKAGE_SEPARATOR = "."

    /**
     * 获取指定包下所有类对象的集合
     * @param pkgName 包名(com.demo.controller)
     * @param recursive 是否递归遍历子目录
     * @param innerContains 是否包含内部类
     * @return 类集合
     */
    fun scanClasses(pkgName: String, recursive: Boolean = true, innerContains: Boolean = true): Set<Class<*>> {
        val classesSet: MutableSet<Class<*>> = HashSet()
        val allClassFileString = getClassName(pkgName, recursive, innerContains)
        for (curFileString in allClassFileString) {
            try {
                classesSet.add(getClassObj(curFileString))
            } catch (e: ClassNotFoundException) {
                logger.error("load class fail", e)
            }
        }
        return classesSet
    }


    /**
     * 加载类
     * @param className 类名
     * @return
     */
    private fun getClassObj(className: String): Class<*> {
        return this::class.java.classLoader.loadClass(className)
    }

    /**
     * 查找包下的所有类的名字
     * @param packageName
     * @param recursive 是否递归遍历
     * @param innerContains 是否包含内部类
     * @return List集合，内容为类的全名
     */
    fun getClassName(packageName: String, recursive: Boolean = true, innerContains: Boolean = true): List<String> {
        val result: MutableList<String> = ArrayList()
        val suffixPath = packageName.replace("\\.".toRegex(), "/")
        val loader = Thread.currentThread().contextClassLoader
        val urls = loader.getResources(suffixPath)
        while (urls.hasMoreElements()) {
            urls.nextElement()?.let { url ->
                when (url.protocol) {
                    "file" -> {
                        result.addAll(getAllClassNameByFile(File(url.path), recursive, innerContains))
                    }
                    "jar" -> {
                        result.addAll(getAllClassNameByJar((url.openConnection() as JarURLConnection).jarFile, packageName, recursive, innerContains))
                    }
                    else -> {
                        return@let
                    }
                }
            }
        }
        return result
    }

    private fun isClassFile(file: File, call: (String) -> Unit): Boolean {
        if (file.isFile) {
            val path = file.path
            if (path.endsWith(CLASS_SUFFIX)) {
                val classPath = path.replace(CLASS_SUFFIX, "")
                val className = classPath.substring(classPath.indexOf(CLASS_FILE_PREFIX) + CLASS_FILE_PREFIX.length)
                        .replace(File.separator, PACKAGE_SEPARATOR)
                call(className)
                return true
            }
        }
        return false
    }

    /**
     * 获取所有class文件的名字
     * @param file
     * @param recursive  是否迭代遍历
     * @param innerContains 是否包含内部类
     * @return List
     */
    private fun getAllClassNameByFile(file: File, recursive: Boolean = true, innerContains: Boolean = true): List<String> {
        if (!file.exists()) {
            return emptyList()
        }
        val classNames: MutableList<String> = java.util.ArrayList()
        if (!isClassFile(file) {
                    if (innerContains || !it.contains("$")) {
                        classNames.add(it)
                    }
                }
        ) {
            file.listFiles()?.let { files ->
                files.forEach {
                    if (recursive) {
                        classNames.addAll(getAllClassNameByFile(it, recursive, innerContains))
                    } else {
                        isClassFile(it) { className ->
                            if (innerContains || !className.contains("$")) {
                                classNames.add(className)
                            }
                        }
                    }
                }
            }
        }
        return classNames
    }

    /**
     * 递归获取jar所有class文件的名字
     * @param jarFile
     * @param packageName 包名
     * @param recursive  是否迭代遍历
     * @param innerContains 是否包含内部类
     * @return List
     */
    private fun getAllClassNameByJar(jarFile: JarFile, packageName: String, recursive: Boolean, innerContains: Boolean = true): List<String> {
        val result: MutableList<String> = java.util.ArrayList()
        val entries = jarFile.entries()
        while (entries.hasMoreElements()) {
            val jarEntry = entries.nextElement()
            var name = jarEntry.name
            if (name.endsWith(CLASS_SUFFIX)) {
                name = name.replace(CLASS_SUFFIX, "").replace("/", ".")
                if (recursive) {
                    if (name.startsWith(packageName) && (innerContains || !name.contains("$"))) {
                        result.add(name)
                    }
                } else {
                    if (packageName == name.substring(0, name.lastIndexOf(".")) && (innerContains || !name.contains("$"))) {
                        result.add(name)
                    }
                }
            }
        }
        return result
    }
}

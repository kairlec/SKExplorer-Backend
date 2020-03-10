package com.kairlec.utils.file

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path

object GetFileContent {
    fun byPath(path: Path): String {
        return byFile(path.toFile())
    }

    fun byPathString(Path: String): String {
        return byFile(File(Path))
    }

    fun byFile(file: File): String {
        if (!file.exists()) {
            return ""
        }
        val stringBuilder = StringBuilder()
        try {
            FileReader(file).use { fileReader ->
                BufferedReader(fileReader).use { bufferedReader ->
                    while (!bufferedReader.ready()) {
                        Thread.sleep(10)
                    }
                    var tempStr: String?
                    while (bufferedReader.readLine().also { tempStr = it } != null) {
                        stringBuilder.append(tempStr)
                        stringBuilder.append(System.lineSeparator())
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return ""
        } catch (e: InterruptedException) {
            e.printStackTrace()
            return ""
        }
        return stringBuilder.toString()
    }
}

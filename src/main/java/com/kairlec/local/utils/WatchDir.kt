package com.kairlec.local.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.IOException
import java.nio.file.*
import java.util.*

object WatchDir {
    private val logger = LogManager.getLogger(WatchDir::class.java)
    private val watcher: WatchService = FileSystems.getDefault().newWatchService()
    private val keys: MutableSet<RegisteredPath> = HashSet()

    data class RegisteredPath(
            val key: WatchKey,
            val path: Path,
            val eventCall: (Path, WatchEvent.Kind<out Any>) -> Unit,
            val enableWatchEvent: Array<out WatchEvent.Kind<*>>
    ) {
        fun sameWatch(other: Any): Boolean {
            if (other is Path) {
                if (Files.notExists(other) || Files.notExists(this.path)) {
                    return other.toAbsolutePath().toString() == this.path.toAbsolutePath().toString()
                }
                return Files.isSameFile(other, this.path)
            }
            if (other is WatchKey) {
                return this.key == other
            }
            if (other is RegisteredPath) {
                if (this.key == other.key) {
                    return true
                }
                if (Files.notExists(other.path) || Files.notExists(this.path)) {
                    return other.path.toAbsolutePath().toString() == this.path.toAbsolutePath().toString()
                }
                return Files.isSameFile(other.path, this.path)
            }
            return false
        }

        override fun equals(other: Any?): Boolean {
            return other?.let { sameWatch(it) } ?: false
        }
    }

    private fun walk(file: File, call: (File) -> Unit) {
        if (file.isDirectory) {
            file.walk().onEnter { true }
                    .onFail { errorFile: File, e: IOException -> run { logger.error("""walk to file "$errorFile" throwed a exception:${e.message}""", e) } }
                    .filter { it.isDirectory }
                    .forEach {
                        call(it)
                    }
        } else {
            call(file)
        }
    }

    fun register(file: Path, vararg enableWatchEvent: WatchEvent.Kind<*> = arrayOf(StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE), call: (Path, WatchEvent.Kind<out Any>) -> Unit) {
        val key = if (Files.isDirectory(file)) {
            file.register(watcher, *enableWatchEvent)
        } else {
            file.parent.register(watcher, *enableWatchEvent)
        }
        keys.add(RegisteredPath(key, file, call, enableWatchEvent))
    }

    fun revoke(file: Path) {
        keys.removeIf { it.sameWatch(file) }
    }


    fun revokeAll(file: File) {
        walk(file) { revoke(it.toPath()) }
    }

    fun registerAll(file: File, vararg enableWatchEvent: WatchEvent.Kind<*> = arrayOf(StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE), call: (Path, WatchEvent.Kind<out Any>) -> Unit) {
        walk(file) { register(it.toPath(), *enableWatchEvent, call = call) }
    }

    init {
        GlobalScope.async(Dispatchers.IO) {
            try {
                while (true) {
                    val key = watcher.take()
                    val registeredPath = keys.find { it.sameWatch(key) } ?: continue
                    val dir = if (Files.isDirectory(registeredPath.path)) registeredPath.path else registeredPath.path.parent
                    for (event in key.pollEvents()) {
                        val kind = event.kind()
                        if (kind == StandardWatchEventKinds.OVERFLOW) {
                            continue
                        }
                        val name = event.context() as Path
                        val child = dir.resolve(name)
                        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                            try {
                                registerAll(child.toFile(), *registeredPath.enableWatchEvent, call = registeredPath.eventCall)
                            } catch (e: IOException) {
                                logger.error("""error in file "$child" : ${e.message}""", e)
                            }
                        }
                        keys.find { it.sameWatch(child)||it.sameWatch(dir) }?.let {
                            registeredPath.eventCall.invoke(child, kind)
                        }
                    }
                    if (!key.reset()) {
                        logger.error("Watch file Service reset error:${registeredPath.path}")
                        revokeAll(registeredPath.path.toFile())
                    }
                    delay(100)
                }
            } catch (e: Exception) {
                logger.error("Error in watch file", e)
            }
        }
    }
}
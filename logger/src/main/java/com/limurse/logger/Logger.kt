package com.limurse.logger

import com.limurse.logger.config.Config
import com.limurse.logger.file.FileWriter
import com.limurse.logger.file.LogLevel
import com.limurse.logger.threading.ThreadQueue
import com.limurse.logger.util.FileZipper
import android.util.Log
import java.io.File

object Logger {

    private var initialized = false
    private var isEnable: Boolean = true

    private var config: Config? = null
    private val fileWriter: FileWriter? by lazy {
        return@lazy config?.let {
            return@let FileWriter(
                it.directory,
                it.dataFormatterPattern,
                it.startupData
            )
        }
    }
    private val fileZipper: FileZipper by lazy {
        FileZipper()
    }

    private var logQueue: ThreadQueue = ThreadQueue("LogQueue")

    fun init(config: Config) {
        if (initialized) {
            return
        }

        Logger.config = config
        initialized = true
    }

    fun i(tag: String? = config?.defaultTag, msg: String) = checkBlock {
        if (config?.logcatEnable == true) {
            Log.i(tag, msg)
        }

        postLog(LogLevel.Info, tag, msg)
    }

    fun e(
        tag: String? = config?.defaultTag,
        msg: String,
        throwable: Throwable? = null
    ) = checkBlock {
        if (config?.logcatEnable == true) {
            Log.e(tag, msg, throwable)
        }

        postLog(logLevel = LogLevel.Error, tag = tag, msg = msg, throwable = throwable)
    }

    fun e(
        tag: String? = config?.defaultTag,
        throwable: Throwable
    ) = checkBlock {
        if (config?.logcatEnable == true) {
            Log.e(tag, "", throwable)
        }

        postLog(logLevel = LogLevel.Error, tag = tag, throwable = throwable)
    }

    fun w(
        tag: String? = config?.defaultTag,
        msg: String
    ) = checkBlock {
        if (config?.logcatEnable == true) {
            Log.w(tag, msg)
        }

        postLog(logLevel = LogLevel.Warning, tag = tag, msg = msg)
    }

    fun d(
        tag: String? = config?.defaultTag,
        msg: String
    ) = checkBlock {
        if (config?.logcatEnable == true) {
            Log.d(tag, msg)
        }

        postLog(logLevel = LogLevel.Debug, tag = tag, msg = msg)
    }

    private fun postLog(
        logLevel: LogLevel,
        tag: String? = config?.defaultTag,
        msg: String? = "",
        throwable: Throwable? = null
    ) = fileWriter?.let { writer ->
        logQueue.postRunnable {
            val stringBuilder = StringBuilder("$logLevel/$tag: $msg \n")

            throwable?.let { exception ->
                exception.stackTrace.forEach { element ->
                    stringBuilder.append("\t $element\n")
                }
            }

            writer.write(stringBuilder.toString())
        }
    }

    fun setEnable(isEnable: Boolean) {
        Logger.isEnable = isEnable
    }

    fun deleteFiles() = checkBlock {
        i(msg = "Logger delete files called")
        fileWriter?.deleteLogsDir()
    }

    fun compressLogsInZipFile(
        zipFileName: String? = null,
        callback: ((zipFile: File?) -> Unit),
    ) = checkBlock {
        config?.let {
            fileZipper.compressFiles(it, zipFileName, callback)
        }
    }

    private fun checkBlock(block: () -> Unit) {
        if (initialized && isEnable) {
            block()
        } else if (isEnable) {
            Log.e(
                javaClass.simpleName,
                "SDK not initialized maybe forgot call Logger.init(config: Config)"
            )
        }
    }
}
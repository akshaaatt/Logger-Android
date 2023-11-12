package com.limurse.logger.file

import com.limurse.logger.config.Constance.Companion.DIRECTORY
import com.limurse.logger.time.FastDateFormat
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.util.*

internal class FileWriter(
    private val directory: String,
    dataFormatterPattern: String,
    startLogs: Map<String, String>?,
) {
    private var streamWriter: OutputStreamWriter? = null
    private var dateFormat: FastDateFormat? = null
    private var logFile: File? = null

    init {
        dateFormat = FastDateFormat.getInstance(dataFormatterPattern, Locale.US)
        try {
            val file = File(directory)
            val logDir = File(file.absolutePath + DIRECTORY)
            if (logDir.exists().not()) {
                logDir.mkdirs()
            }

            logFile = File(logDir, "${dateFormat?.format(System.currentTimeMillis())}.txt")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            logFile?.createNewFile()
            val stream = FileOutputStream(logFile)
            streamWriter = OutputStreamWriter(stream).apply {
                write("Logger started at ${dateFormat?.format(System.currentTimeMillis())}\n")
                startLogs?.forEach {
                    write("${it.key}: ${it.value} \n")
                }
                write("\n\n")
                flush()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun write(message: String) {
        streamWriter.takeIf { dateFormat != null }?.let { writer ->
            try {
                writer.write("${dateFormat?.format(System.currentTimeMillis())} $message")
                writer.flush()
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "e:", e)
            }
        }
    }

    fun deleteLogsDir() {
        val currentFile = File(directory)
        val logDir = File(currentFile.absolutePath + DIRECTORY)

        logDir.listFiles()?.filter {
            it.absolutePath != logFile?.absolutePath
        }?.forEach {
            it.delete()
        }
    }
}
package com.limurse.logger.util

import com.limurse.logger.Logger
import com.limurse.logger.config.Config
import com.limurse.logger.config.Constants
import com.limurse.logger.threading.ThreadQueue
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

internal class FileZipper {
    private var logQueue: ThreadQueue = ThreadQueue("CompressThread")

    fun compressFiles(
        config: Config,
        zipFileName: String? = null,
        callback: ((file: File?) -> Unit),
    ) = logQueue.postRunnable {
        try {
            val fileName = zipFileName?.replace(".", "_")?.replace("/", "_")
            val logFileDirectory = File(config.directory)
            val logDirectory = File(logFileDirectory.absolutePath + Constants.DIRECTORY)
            val zipFile = File(logDirectory, "${fileName ?: "Logs"}.zip")
            if (zipFile.exists()) {
                zipFile.delete()
            }
            val logFiles = logDirectory.listFiles()
            var inputStream: BufferedInputStream? = null
            var zipOutputStream: ZipOutputStream? = null
            try {
                val zipFileStream = FileOutputStream(zipFile)
                zipOutputStream = ZipOutputStream(BufferedOutputStream(zipFileStream))
                val data = ByteArray(1024 * 64)
                logFiles?.forEach { currentFile ->
                    val fileInputStream = FileInputStream(currentFile)
                    var count: Int

                    inputStream = BufferedInputStream(fileInputStream, data.size).also { stream ->
                        val entry = ZipEntry(currentFile.name)
                        zipOutputStream.putNextEntry(entry)
                        while (stream.read(data, 0, data.size).also { count = it } != -1) {
                            zipOutputStream.write(data, 0, count)
                        }
                    }

                    inputStream?.close()
                    inputStream = null
                }
            } catch (e: Exception) {
                Logger.e(throwable = e)
                callback(null)
            } finally {
                inputStream?.close()
                zipOutputStream?.close()
            }
            callback(zipFile)
        } catch (e: Exception) {
            Logger.e(throwable = e)
            callback(null)
        }
    }
}
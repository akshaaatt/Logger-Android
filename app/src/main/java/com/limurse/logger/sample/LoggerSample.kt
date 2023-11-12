package com.limurse.logger.sample

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.limurse.logger.Logger
import com.limurse.logger.config.Config
import com.limurse.logger.sample.databinding.ActivityMainBinding
import com.limurse.logger.util.FileIntent
import java.io.File

class LoggerSample : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var zipFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeLogger()
        setupButtonListeners()
    }

    private fun initializeLogger() {
        val logDirectory = applicationContext.getExternalFilesDir(null)?.path.orEmpty()
        val config = Config.Builder(logDirectory)
            .setDefaultTag("SampleAppLog")
            .setLogcatEnable(true)
            .setStartupData(collectStartupData())
            .build()

        Logger.init(config)
    }

    private fun collectStartupData(): Map<String, String> = mapOf(
        "App Version" to System.currentTimeMillis().toString(),
        "Device Application Id" to BuildConfig.APPLICATION_ID,
        "Device Version Code" to BuildConfig.VERSION_CODE.toString(),
        "Device Version Name" to BuildConfig.VERSION_NAME,
        "Device Build Type" to BuildConfig.BUILD_TYPE,
        "Device" to android.os.Build.DEVICE,
        "Device SDK" to android.os.Build.VERSION.SDK_INT.toString(),
        "Device Manufacturer" to android.os.Build.MANUFACTURER
    )

    private fun setupButtonListeners() {
        with(binding) {
            init.setOnClickListener { showLog("Logger initialized") }
            writeNormalLog.setOnClickListener { writeSampleLogs() }
            writeExceptionLog.setOnClickListener { writeExceptionLog() }
            deleteLogs.setOnClickListener { deleteLogs() }
            zipLogs.setOnClickListener { zipLogs() }
            emailLogs.setOnClickListener { emailLogs() }
        }
    }

    private fun writeSampleLogs() {
        // Sample log messages
        Logger.i("Custom", "This is an Info Log with custom TAG")
        Logger.d(msg = "This is a Debug Log")
        Logger.w(msg = "This is a Warning Log")
        Logger.e(msg = "This is an Error Log")

        showLog("Sample logs written")
    }

    private fun writeExceptionLog() {
        try {
            throw Exception("Sample Exception")
        } catch (e: Exception) {
            Logger.e("Exception Log", e)
            showLog("Exception log written")
        }
    }

    private fun deleteLogs() {
        Logger.deleteFiles()
        showLog("Logs deleted")
    }

    private fun zipLogs() {
        Logger.compressLogsInZipFile { zipFile ->
            this.zipFile = zipFile
            val message = if (zipFile?.exists() == true) "Zip file created successfully" else "Error creating zip file"
            showLog(message)
        }
    }

    private fun emailLogs() {
        val zipFile = this.zipFile
        if (zipFile != null && zipFile.exists()) {
            val intent = FileIntent.fromFile(this, zipFile, BuildConfig.APPLICATION_ID)
            intent?.apply {
                putExtra(Intent.EXTRA_SUBJECT, "Log Files")
                putExtra(Intent.EXTRA_TEXT, "Please find the attached log files.")
                putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this@LoggerSample, "${BuildConfig.APPLICATION_ID}.provider", zipFile))
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(Intent.createChooser(this, "Email logs..."))
            }
        } else {
            showLog("No zip file found. Create one first.")
        }
    }

    private fun showLog(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
package com.limurse.logger.sample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.limurse.logger.Logger;
import com.limurse.logger.config.Config;
import com.limurse.logger.sample.databinding.ActivityMainBinding;
import com.limurse.logger.util.FileIntent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class LoggerJavaSample extends AppCompatActivity {

    private ActivityMainBinding binding;
    private File zipFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeLogger();
        setupButtonListeners();
    }

    private void initializeLogger() {
        String logDirectory = getApplicationContext().getExternalFilesDir(null) != null
                ? Objects.requireNonNull(getApplicationContext().getExternalFilesDir(null)).getPath()
                : "";
        Config config = new Config.Builder(logDirectory)
                .setDefaultTag("SampleAppLog")
                .setLogcatEnable(true)
                .setStartupData(collectStartupData())
                .build();

        Logger.INSTANCE.init(config);
    }

    private Map<String, String> collectStartupData() {
        Map<String, String> startupData = new HashMap<>();
        startupData.put("App Version", String.valueOf(System.currentTimeMillis()));
        startupData.put("Device Application Id", BuildConfig.APPLICATION_ID);
        startupData.put("Device Version Code", String.valueOf(BuildConfig.VERSION_CODE));
        startupData.put("Device Version Name", BuildConfig.VERSION_NAME);
        startupData.put("Device Build Type", BuildConfig.BUILD_TYPE);
        startupData.put("Device", android.os.Build.DEVICE);
        startupData.put("Device SDK", String.valueOf(android.os.Build.VERSION.SDK_INT));
        startupData.put("Device Manufacturer", android.os.Build.MANUFACTURER);
        return startupData;
    }

    private void setupButtonListeners() {
        binding.init.setOnClickListener(v -> showToast("Logger initialized"));
        binding.writeNormalLog.setOnClickListener(v -> writeSampleLogs());
        binding.writeExceptionLog.setOnClickListener(v -> writeExceptionLog());
        binding.deleteLogs.setOnClickListener(v -> deleteLogs());
        binding.zipLogs.setOnClickListener(v -> zipLogs());
        binding.emailLogs.setOnClickListener(v -> emailLogs());
    }

    private void writeSampleLogs() {
        Logger.INSTANCE.i("Custom", "This is an Info Log with custom TAG");
        Logger.INSTANCE.d("Custom","This is a Debug Log");
        Logger.INSTANCE.w("Custom","This is a Warning Log");
        Logger.INSTANCE.e("Custom", new Throwable("This is an Error Log with custom TAG"));
        showToast("Sample logs written");
    }

    private void writeExceptionLog() {
        try {
            throw new Exception("Sample Exception");
        } catch (Exception e) {
            Logger.INSTANCE.e("Exception Log", e);
            showToast("Exception log written");
        }
    }

    private void deleteLogs() {
        Logger.INSTANCE.deleteFiles();
        showToast("Logs deleted");
    }

    private void zipLogs() {
        Logger.INSTANCE.compressLogsInZipFile(null, newZipFile -> {
            zipFile = newZipFile;
            String message = (zipFile != null && zipFile.exists())
                    ? "Zip file created successfully"
                    : "Error creating zip file";
            showToast(message);
            return null;
        });
    }

    private void emailLogs() {
        if (zipFile != null && zipFile.exists()) {
            Intent intent = FileIntent.fromFile(this, zipFile, BuildConfig.APPLICATION_ID);
            if (intent != null) {
                intent.putExtra(Intent.EXTRA_SUBJECT, "Log Files");
                intent.putExtra(Intent.EXTRA_TEXT, "Please find the attached log files.");
                intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.provider", zipFile));
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(intent, "Send Email..."));
            }
        } else {
            showToast("No zip file found. Create one first.");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
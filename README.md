<h1 align="center">Logger</h1>

<p align="center">
    <img src="https://jitpack.io/v/akshaaatt/Logger-Android.svg?style=flat-square&logo=github&logoColor=white"
         alt="GitHub issues">
    <a href="https://jitpack.io/#akshaaatt/Logger-Android">
    <a href="https://github.com/akshaaatt/Logger-Android/commits/master">
    <img src="https://img.shields.io/github/last-commit/akshaaatt/Logger-Android.svg?style=flat-square&logo=github&logoColor=white"
         alt="GitHub last commit">
    <a href="https://github.com/akshaaatt/Logger-Android/issues">
    <img src="https://img.shields.io/github/issues-raw/akshaaatt/Logger-Android.svg?style=flat-square&logo=github&logoColor=white"
         alt="GitHub issues">
    <a href="https://github.com/akshaaatt/Logger-Android/pulls">
    <img src="https://img.shields.io/github/issues-pr-raw/akshaaatt/Logger-Android.svg?style=flat-square&logo=github&logoColor=white"
         alt="GitHub pull requests"/>
</p>

<p align="center">
  <a href="#features">Features</a> •
  <a href="#development">Development</a> •
  <a href="#usage">Usage</a> •
  <a href="#license">License</a> •
  <a href="#contribution">Contribution</a>
</p>
	    
---

[![ezgif-com-video-to-gif.gif](https://i.postimg.cc/BQY0T97N/ezgif-com-video-to-gif.gif)](https://postimg.cc/RNHym8Bn)

Android Library for easing logging and to store loges in Files

## Features

* Written in Kotlin
* No boilerplate code
* Easy initialization
* Supports InApp & Subscription products
* Simple configuration for consumable products

## Gradle Dependency

* Add the JitPack repository to your project's build.gradle file

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
* Add the dependency in your app's build.gradle file

```groovy
dependencies {
    implementation 'com.github.akshaaatt:Logger-Android:1.0.0'
}
```

## Development

* Prerequisite: Latest version of the Android Studio and SDKs on your pc.
* Clone this repository.
* Use the `gradlew build` command to build the project directly or use the IDE to run the project to your phone or the emulator.

## Usage

**Init:**
```kotlin
val config = Config.Builder(it.path)
    .setDefaultTag("TAG")
    .setLogcatEnable(true)
    .setDataFormatterPattern("dd-MM-yyyy-HH:mm:ss")
    .setStartupData(
        mapOf(
            "App Version" to "${BuildConfig.VERSION}",
            "Device Application Id" to BuildConfig.APPLICATION_ID,
            "Device Version Code" to BuildConfig.VERSION_CODE.toString(),
            "Device Version Name" to BuildConfig.VERSION_NAME,
            "Device Build Type" to BuildConfig.BUILD_TYPE,
            "Device" to Build.DEVICE,
            "Device SDK" to Build.VERSION.SDK_INT.toString(),
            "Device Manufacturer" to Build.MANUFACTURER
        )
    ).build()

Logger.init(config)
```
**Log:**
```kotlin
Logger.i("TAG", "This is normal Log with custom TAG")
Logger.i(msg = "This is normal Info Log")
Logger.d(msg = "This is normal Debug Log")
Logger.w(msg = "This is normal Warning Log")
Logger.e(msg = "This is normal Error Log")
```

**Exception:**
```kotlin
try {
    //...
} catch (e: Exception) {
    Logger.e(msg = "log message", throwable = e)
}
```

**Compress to Zip file and Email logs:**
```kotlin
Logger.apply {
    compressLogsInZipFile { zipFile ->
        zipFile?.let {
            FileIntent.fromFile(
                this@MainActivity,
                it,
                BuildConfig.APPLICATION_ID
            )?.let { intent ->
                intent.putExtra(Intent.EXTRA_SUBJECT, "Log File")
                try {
                    startActivity(Intent.createChooser(intent, "Email logs..."))
                } catch (e: java.lang.Exception) {
                    e(throwable = e)
                }
            }
        }
    }
}
```
for share file with email or etc add this provider in the AndroidManifest.xml file:
```xml
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.provider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/provider_paths"/>
</provider>
```
And this one in resource/xml/provider_paths:
```xml
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <external-path name="media" path="."/>
</paths>
```

**Delete log files:**
```kotlin
Logger.deleteFiles()
```

**Enable and disable logging:**
```kotlin
Logger.setEnable(boolean)
```

#### Inspired [by](https://github.com/aabolfazl/FileLogger)

## License

This Project is licensed under the [GPL version 3 or later](https://www.gnu.org/licenses/gpl-3.0.html).

## Contribution

You are most welcome to contribute to this project!

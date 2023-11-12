package com.limurse.logger.config

import com.limurse.logger.config.Constants.Companion.DEFAULT_PATTERN
import com.limurse.logger.config.Constants.Companion.DEFAULT_TAG
import com.limurse.logger.config.Constants.Companion.LOGCAT_ENABLE

class Config private constructor(
    val directory: String,
    val defaultTag: String,
    val logcatEnable: Boolean,
    val dataFormatterPattern: String,
    val startupData: Map<String, String>?,
) {

    class Builder(private val directory: String) {
        private var defaultTag: String = DEFAULT_TAG
        private var logcatEnable: Boolean = LOGCAT_ENABLE
        private var dataFormatterPattern: String = DEFAULT_PATTERN
        private var startupData: Map<String, String>? = null

        fun setDefaultTag(defaultTag: String) = apply { this.defaultTag = defaultTag }
        fun setLogcatEnable(logcatEnable: Boolean) = apply { this.logcatEnable = logcatEnable }
        fun setStartupData(startupData: Map<String, String>?) = apply { this.startupData = startupData }

        fun setDataFormatterPattern(pattern: String) = apply {
            this.dataFormatterPattern = pattern.replace("/", "-")
                .replace(" ", "")
                .trim()

            if (pattern.isEmpty().or(pattern.contains("/"))) {
                this.dataFormatterPattern = DEFAULT_PATTERN
            }
        }

        fun build() = Config(
            directory,
            defaultTag,
            logcatEnable,
            dataFormatterPattern,
            startupData,
        )
    }
}
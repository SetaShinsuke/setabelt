package com.seta.common.logs

import android.content.Context
import android.os.Environment
import android.util.Log
import com.seta.common.utils.Constants
import de.mindpipe.android.logging.log4j.LogConfigurator
import org.apache.log4j.Level
import java.io.File

/**
 * Created by SETA_WORK on 2017/7/17.
 */
object LogConfig{
    fun configLogger(context: Context, logFileName: String) {
        val logConfigurator = LogConfigurator()
        if (!getStorageState()) {
            Log.i(Constants.LOG_TAG_S, "Logger init fail : storage state not mounted.")
            return
        }
        val dir = context.getExternalFilesDir(null)
        dir ?: throw NullPointerException("Logger 初始化失败, File dir 为空!")
        val parentDir = dir.parentFile
        with(logConfigurator) {
            fileName = parentDir.absolutePath + File.separator + logFileName
            rootLevel = Level.TRACE
            isUseLogCatAppender = false
            setLevel("org.apache", Level.ERROR)
            configure()
        }
    }

    fun getStorageState(): Boolean {
        val state = Environment.getExternalStorageState()
        return state == Environment.MEDIA_MOUNTED
    }
}
package com.seta.common.logs

import android.content.Context
import android.util.Log
import com.seta.common.extensions.isDebuggable
import com.seta.common.utils.Constants
import org.apache.log4j.Logger

/**
 * Created by SETA_WORK on 2017/7/4.
 */
object LogX {

    val logger: Logger by lazy { Logger.getLogger("Log") }
    var logLevel = Log.VERBOSE
    var logFileLevel = Log.INFO
    var logTag = Constants.LOG_TAG_S

    fun init(context: Context, logFileName: String) {
        LogConfig.configLogger(context, logFileName)
        logger.trace("Log Init")
        if (context.isDebuggable()) {
            logLevel = Log.VERBOSE
            logFileLevel = Log.VERBOSE
        } else {
            logLevel = Log.DEBUG
            logFileLevel = Log.DEBUG
        }
    }

    private fun trace2File(tag: String, msg: String, throwable: Throwable? = null) {
        if (throwable == null) {
            logger.trace(tag + " " + msg)
        } else {
            logger.trace(tag + " " + msg, throwable)
        }
    }

    fun fastLog(msg: String) {
        Log.v(logTag, msg)
    }

    fun mark() {
        e("有调试代码未删除!!!")
    }

    fun v(s: String, throwable: Throwable? = null) {
        v(logTag, s, throwable)
    }

    fun v(tag: String = logTag, s: String, throwable: Throwable? = null) {
        if (logLevel <= Log.VERBOSE) {
            Log.v(tag, s, throwable)
            if (logFileLevel <= Log.VERBOSE) {
                trace2File(tag, s, throwable)
            }
        }
    }

    fun d(s: String, throwable: Throwable? = null) {
        d(logTag, s, throwable)
    }

    fun d(tag: String = logTag, s: String, throwable: Throwable? = null) {
        if (logLevel <= Log.DEBUG) {
            Log.d(tag, s, throwable)
            if (logFileLevel <= Log.DEBUG) {
                trace2File(tag, s, throwable)
            }
        }
    }

    fun i(s: String, throwable: Throwable? = null) {
        i(logTag, s, throwable)
    }

    fun i(tag: String = logTag, s: String, throwable: Throwable? = null) {
        if (logLevel <= Log.INFO) {
            Log.i(tag, s, throwable)
            if (logFileLevel <= Log.INFO) {
                trace2File(tag, s, throwable)
            }
        }
    }

    fun w(s: String, throwable: Throwable? = null) {
        w(logTag, s, throwable)
    }

    fun w(tag: String = logTag, s: String, throwable: Throwable? = null) {
        if (logLevel <= Log.WARN) {
            Log.w(tag, s, throwable)
            if (logFileLevel <= Log.WARN) {
                trace2File(tag, s, throwable)
            }
        }
    }

    fun e(s: String, throwable: Throwable? = null) {
        e(logTag, s, throwable)
    }

    fun e(tag: String = logTag, s: String, throwable: Throwable? = null) {
        if (logLevel <= Log.ERROR) {
            Log.e(tag, s, throwable)
            if (logFileLevel <= Log.ERROR) {
                trace2File(tag, s, throwable)
            }
        }
    }
}
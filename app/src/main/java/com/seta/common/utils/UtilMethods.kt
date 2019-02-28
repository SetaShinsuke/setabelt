package com.seta.common.utils

import android.content.Context
import android.os.Environment
import com.seta.common.logs.LogX
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException



/**
 * Created by SETA_WORK on 2017/7/17.
 */
object UtilMethods {
    fun exportDb(context: Context, dbName: String): String? {
        LogX.d("Export db, dbName : $dbName")
        val sd = Environment.getExternalStorageDirectory()
        val data = Environment.getDataDirectory()
        val currentDBPath = "/data/" + context.packageName + "/databases/" + dbName
        val backupDBPath = "bk_" + dbName
        val currentDb = File(data, currentDBPath)
        val backupDb = File(sd, backupDBPath)
        LogX.d("db path : $currentDBPath || Backup path : $backupDBPath")
        try {
            val source = FileInputStream(currentDb).channel
            val destination = FileOutputStream(backupDb).channel
            destination.transferFrom(source, 0, source.size())
            source.close()
            destination.close()
            return backupDBPath
        } catch (e: IOException) {
            e.printStackTrace()
            LogX.e("Error when exporting database : ${e.message}")
            return null
        }
    }
}
package com.seta.common.framework

import android.app.Application
import com.seta.common.extensions.DelegateExt
import com.seta.common.logs.LogX
import com.seta.setabelt.utils.Constants.LOG_FILE_NAME


/**
 * Created by SETA_WORK on 2017/7/3.
 */
class BaseApplication : Application() {

    companion object {
        var instance: BaseApplication by DelegateExt.notNullSingleValue() //非空、懒加载
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        //初始化log
        LogX.init(this, LOG_FILE_NAME)
        LogX.d("Application onCreate().")
    }
}
package com.seta.common.interfaces

/**
 * Created by SETA_WORK on 2017/8/30.
 */
interface ResultHandler {
    fun <T> onSuccess(result: T)
    fun onFail(t: Throwable)
}
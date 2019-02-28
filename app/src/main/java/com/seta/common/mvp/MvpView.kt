package com.seta.common.mvp

/**
 * Created by SETA_WORK on 2017/7/4.
 */
interface MvpView

//interface MvpView {
//    fun onLoadFail(e: Throwable)
//}

interface PagingMvpView : MvpView {
    fun showEmpty(hasData: Boolean = true)
}
package com.seta.common.framework

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.greenrobot.eventbus.EventBus

abstract class BaseFragment : Fragment() {

    var mRootView: View? = null
    var mActivity: Activity? = null
    var refreshOnStart = false
    private var dataLoadOnce = false //onStart中默认加载一次，无视refreshOnStart

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = initView(inflater)
        mActivity = activity
        return mRootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initData()
        initListener()
        super.onActivityCreated(savedInstanceState)
    }

    fun registerBus() {
        EventBus.getDefault().register(this)
    }

    fun postEvent(event: Any) {
        EventBus.getDefault().post(event)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.onDying()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onStart() {
        super.onStart()
        if (!dataLoadOnce || refreshOnStart) {
            dataLoadOnce = true
            refreshData()
        }
    }

    open fun onBackPressHandled(): Boolean {
        return false
    }

    /**
     * @param inflater
     * @return
     * @des 初始化view，需要子类复写
     */
    abstract fun initView(inflater: LayoutInflater): View

    /**
     * 销毁时进行清理工作
     */
    abstract fun onDying()

    open fun refreshData() {
    }

    /**
     * 清除数据，用于退出切换账户等
     */
    open fun clearData() {
    }

    /**
     * @des 初始化数据
     */
    open fun initData() {}

    /**
     * @des 初始化事件
     */
    open fun initListener() {}
}
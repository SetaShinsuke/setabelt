package com.seta.common.views

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class NoSwipePager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {
    var swipeEnabled: Boolean = true

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //检测点击?
        return if (this.swipeEnabled) {
            super.onTouchEvent(event)
        } else false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (this.swipeEnabled) {
            super.onInterceptTouchEvent(event)
        } else false
    }
}

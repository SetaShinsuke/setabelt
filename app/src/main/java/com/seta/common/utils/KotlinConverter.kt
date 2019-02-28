package com.seta.common.utils

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Created by SETA_WORK on 2017/7/17.
 * 用来查看 Java 转换成 Kotlin 之后的写法
 *
 * @see KotlinConverter
 */

class KotlinConverter//    private void test(Activity activity){
//        Bundle bundle = new Bundle();
//        bundle.putIntegerArrayList();
//    }

internal class Fr : FrameLayout {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
}



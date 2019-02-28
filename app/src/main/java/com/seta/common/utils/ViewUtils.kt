package com.seta.common.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.seta.common.logs.LogX

/**
 * Created by SETA_WORK on 2017/8/17.
 */
/**
 * 隐藏输入法
 **/
object ViewUtils {
    fun hideInputMethod(view: View) {
        try {
            val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            view.clearFocus()
        } catch (e: Exception) {
            LogX.i("Hide input method error, context : ${view.context.javaClass.simpleName}")
        }
    }
//    public static void hideInputMethod(View view) {
//        try {
//            InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//            view.clearFocus();
////            inputManager.he
//        } catch (Exception e) {
//            e.getStackTrace();
//            Logger.v("hehe", "Hide Input Method Error.Detail : " + e.toString());
//        }
//    }


}
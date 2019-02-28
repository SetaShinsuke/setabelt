package com.seta.common.extensions

import android.app.Activity
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.seta.common.logs.LogX
import com.seta.common.utils.ViewUtils
import kotlinx.coroutines.experimental.android.UI
import org.jetbrains.anko.textColor
import kotlin.coroutines.experimental.CoroutineContext


/**
 * Created by SETA_WORK on 2017/7/3.
 */

//region Activity
fun Activity.logD(content: String, throwable: Throwable? = null) {
    LogX.d(javaClass.simpleName, content, throwable)
}

fun Activity.logE(content: String, throwable: Throwable? = null) {
    LogX.e(javaClass.simpleName, content, throwable)
}

fun Fragment.logD(content: String, throwable: Throwable? = null) {
    LogX.d(javaClass.simpleName, content, throwable)
}

fun Fragment.logE(content: String, throwable: Throwable? = null) {
    LogX.e(javaClass.simpleName, content, throwable)
}

fun BroadcastReceiver.logD(content: String, throwable: Throwable? = null) {
    LogX.d(javaClass.simpleName, content, throwable)
}
//endregion

//region Context
fun Context.toast(textResource: Int, vararg extraString: String?, g: Int? = null) {
    var content = getString(textResource)
    extraString.forEach { content += it }
    Toast.makeText(this, content, Toast.LENGTH_SHORT).apply {
        g?.let { setGravity(g, 0, 0) }
        show()
    }
}

fun Context.getStringFormated(textResource: Int, vararg values: Any?): String? {
//    return String.format(getString(textResource), arrayOf(*values))
    return String.format(getString(textResource), *values)
}

fun Context.getH5Spanned(half1: Int, center: Int, rgb: String, half2: Int): Spanned {

//    val s = "<font color=\"#7ed320\">" + this + "</font>"
    val s = getString(half1) + "<font color=$rgb>" + getString(center) + "</font>" + getString(half2)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(s, Html.FROM_HTML_MODE_LEGACY)
    } else {
        return Html.fromHtml(s)
    }
}

fun Context.isDebuggable(): Boolean = applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0

fun Context.getScreenWidth(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.x
}

fun Context.getScreenHeight(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.y
}

fun Context.getColorRes(resId: Int) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    resources.getColor(resId, theme)
} else {
    resources.getColor(resId)
}

fun Context.sp2Px(sp: Int): Int = sp2Px(sp * 1f).toInt()
fun Context.sp2Px(sp: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics())

fun Context.isAppInstalled(packageName: String): Boolean {
    val pm = packageManager
    try {
        pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
        return true
    } catch (t: Throwable) {
        LogX.e("检查是否安装应用 失败: ${t.message}", t)
    }
    return false;
}

//fun Context.goMarket(packageName: String) {
//    LogX.d("去下载应用")
//    val mAddress = "market://details?id=$packageName"
//    val marketIntent = Intent("android.intent.action.VIEW")
//    marketIntent.data = Uri.parse(mAddress)
//    marketIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//    try {
//        startActivity(marketIntent)
//    } catch (t: Throwable) {
//        LogX.w("去应用市场失败 : " + t.message)
//        val uri = Uri.parse(getString(R.string.market_url, packageName))
//        marketIntent.data = uri //这里面是需要调转的rul
//        try {
//            startActivity(marketIntent)
//        } catch (t2: Throwable) {
//            LogX.e("应用市场网页跳转失败 : $t2")
//        }
//    }
//}

//获取版本号
fun Context.getVersionCode(): Int = try {
    packageManager.getPackageInfo(packageName, 0).versionCode
} catch (e: PackageManager.NameNotFoundException) {
    e.printStackTrace()
    0
}

//获取版本号
fun Context.getVersionName(): String = try {
    packageManager.getPackageInfo(packageName, 0).versionName
} catch (e: PackageManager.NameNotFoundException) {
    e.printStackTrace()
    ""
}
//endregion

fun View.setVisible(boolean: Boolean?) {
    if (boolean == null) {
        visibility = View.GONE
        return
    }
    if (boolean) {
        visibility = View.VISIBLE
    } else {
        visibility = View.GONE
    }
}

fun View.isVisible() = visibility == VISIBLE

fun ProgressDialog.setMessage(resId: Int) = setMessage(context.getString(resId))

//region TextView
fun TextView.underLine() {
    paint.flags = Paint.UNDERLINE_TEXT_FLAG
}

fun TextView.deleteLine(): TextView {
    paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
    return this
}

var TextView.money: Int?
    set(valueCent) {
        if (valueCent == null) {
            text = "￥?"
            return
        }
        text = "￥%.2f".format(valueCent?.let { it * 0.01f })
    }
    get() {
        if (text == "￥?") {
            return null
        }
        try {
            return (text.subSequence(1, text.lastIndex).toString().toFloat() * 100).toInt()
        } catch (e: Exception) {
            return null
        }
    }

fun TextView.txtColor(resId: Int) {
    textColor = context.getColorRes(resId)
}
//endregion

//region EditText
fun EditText.isTextEmpty(): Boolean {
    return text == null || text.toString() == ""
}

fun EditText.cursorLast() {
    setSelection(text.length)
}

fun EditText.fill(text: CharSequence?) {
    setText(text)
    cursorLast()
}

fun EditText.onTextChange(context: CoroutineContext = UI,
                          handler: (s: Editable?) -> Unit) {
    addTextChangeHandler(
            object : TextChangeHandler {
                override fun onTextChange(s: Editable?) {
                    handler(s)
                }
            })
}

fun EditText.addTextChangeHandler(textChangeHandler: TextChangeHandler? = null, onTextChange: ((Editable?) -> Unit)? = null) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            textChangeHandler?.onTextChange(s)
            onTextChange?.invoke(s)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

    })
}

fun EditText.bindClearView(clearView: View) {
    clearView.setOnClickListener { setText("") }
    addTextChangeHandler {
        clearView.setVisible(!it.isNullOrBlank())
    }
}

fun EditText.bindCancelView(cancelView: View) {
    cancelView.setOnClickListener {
        setText("")
        clearFocus()
        ViewUtils.hideInputMethod(it)
    }
    addTextChangeHandler {
        cancelView.setVisible(!it.isNullOrBlank())
    }
}


interface TextChangeHandler {
    fun onTextChange(s: Editable?): Unit
}
//endregion

fun RecyclerView.linear(context: Context, @RecyclerView.Orientation orientation: Int = LinearLayout.VERTICAL) {
    layoutManager = LinearLayoutManager(context, orientation, false)
}

fun Int.dirStr(separator: String = ","): String {
    var divided = this
    var i = this % 1000
    var result = if (divided > 1000) {
        String.format("%03d", i)
    } else {
        i.toString()
    }
    while (divided / 1000 > 0) {
        val part = if (divided / 1000 / 1000 > 0) {
            String.format("%03d", divided / 1000 % 1000)
        } else {
            (divided / 1000 % 1000).toString()
        }
        result = part + separator + result
        divided /= 1000
    }
    return result
}

fun Drawable.addColorFilter(colorInt: Int) {
    colorFilter = PorterDuffColorFilter(colorInt, PorterDuff.Mode.MULTIPLY)
}

fun String.separate(length: Int = 4, separator: String = "\n", reverse: Boolean = false): String {
    var result = ""
    val from = if (reverse) {
        reversed()
    } else {
        this
    }
    from.forEachIndexed { i, c ->
        if (i > 0 && i % length == 0) {
            result += separator
        }
        result += c
    }
    return if (reverse) {
        result.reversed()
    } else {
        result
    }
}

package com.seta.common.extensions

import com.seta.common.logs.LogX
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by SETA_WORK on 2017/7/24.
 */
object DateUtils {
    fun getYMD(year: Int, month: Int, day: Int): String {
        return "$year-$month-$day"
    }

    fun getDateByYMD(year: Int, month: Int, day: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        return Date(calendar.timeInMillis)
    }

    fun ymdToDate(dateStr: String?): Date? = utcToDate(dateStr, "yyyy-MM-dd")

    fun utcToDate(dateStr: String?, formatStr: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"): Date? {
        if (dateStr == null) {
            return null
        }
        val format = SimpleDateFormat(formatStr, Locale.ENGLISH)
        format.timeZone = TimeZone.getTimeZone("UTC")
        var date: Date? = null
        try {
            date = format.parse(dateStr)
            println(date)
        } catch (e: Exception) {
            e.printStackTrace()
            LogX.e("String to date error : $e", e)
        }
        return date
    }

    fun formatDurationString(period: Long, separator: String = ":"): String {
        var duration = period
        if (duration < 0) {
            duration = 0
        }
        LogX.d("duration: $duration")
        val seconds = duration / 1000
        val sec = seconds % 60
        val hour = seconds / 60 / 60
        val min = (seconds - hour * 60 * 60) / 60
//        val hour = duration / 1000 / 60 //小时
//        val min = (duration - hour * 60 * 1000)

        return String.format("%02d$separator%02d$separator%02d", hour, min, sec)
    }

}

fun Date?.format(formatStr: String = "yyyy.MM.dd HH:mm:ss"): String? {
    if (this == null) {
        return ""
    }
    val simpleDate = SimpleDateFormat(formatStr, Locale.ENGLISH)
    val strDt = simpleDate.format(this)
    return strDt
}

fun Date?.toYMD(separator: String = "-"): String {
    if (this == null) {
        return ""
    }
    val cal = Calendar.getInstance()
    cal.time = this
    return cal.toYMD(separator)
//    val simpleDateFormat = SimpleDateFormat.getInstance()
//    return simpleDateFormat.format(this)
}

fun Calendar?.toYMD(separator: String = "-", hasY: Boolean = true, hasM: Boolean = true, hasD: Boolean = true): String {
    if (this == null) {
        return ""
    }
    var result = ""
    if (hasY) result += String.format("%04d", get(Calendar.YEAR))
    if (hasM) result += "$separator${String.format("%02d", get(Calendar.MONTH) + 1)}"
    if (hasD) result += "$separator${String.format("%02d", get(Calendar.DAY_OF_MONTH))}"
    LogX.d("Cal to YMD : $result")
    return result
}

fun Date.startOfDay(): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return Date(cal.timeInMillis)
}

fun Date.endOfDay(): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.set(Calendar.HOUR_OF_DAY, 23)
    cal.set(Calendar.MINUTE, 59)
    cal.set(Calendar.SECOND, 59)
    cal.set(Calendar.MILLISECOND, 999)
    return Date(cal.timeInMillis)
}

fun Calendar.local(): Calendar {
    val hours = TimeUnit.HOURS.convert(this.timeZone.rawOffset.toLong(), TimeUnit.MILLISECONDS)
    this.add(Calendar.HOUR, -hours.toInt())
    return this
}

fun Date.toUtc(): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
    format.timeZone = TimeZone.getTimeZone("UTC")
    return format.format(this.time)
}

fun Calendar.startOfWeek(): Calendar {
    val cal = Calendar.getInstance()
    cal.time = this.time
//    cal.add(Calendar.DAY_OF_MONTH, Calendar.MONDAY - get(Calendar.DAY_OF_WEEK))
//    cal.add(Calendar.WEEK_OF_YEAR, -1)
    cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    return cal
}

fun Calendar.endOfWeek(): Calendar {
    val cal = Calendar.getInstance()
    cal.time = this.time
//    cal.add(Calendar.DAY_OF_MONTH, Calendar.SUNDAY - get(Calendar.DAY_OF_WEEK))
    cal.add(Calendar.WEEK_OF_YEAR, 1)
    cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    return cal
}

fun Calendar.startOfMonth(): Calendar {
    val cal = Calendar.getInstance()
    cal.time = this.time
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH))
    return cal
}

fun Calendar.endOfMonth(): Calendar {
    val cal = Calendar.getInstance()
    cal.time = this.time
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
    return cal
}

fun Calendar.startOfQuarter(): Calendar {
    val cal = Calendar.getInstance()
    cal.time = this.time
    cal.set(Calendar.MONTH, this[Calendar.MONTH] / 3 * 3)
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH))
    return cal
}

fun Calendar.endOfQuarter(): Calendar {
    val cal = Calendar.getInstance()
    cal.time = this.time
    cal.set(Calendar.MONTH, this[Calendar.MONTH] / 3 * 3 + 2)
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
    return cal
}

fun Calendar.getMonthsOfQuarter(): ArrayList<Int> {
    return arrayListOf(startOfQuarter()[Calendar.MONTH], startOfQuarter()[Calendar.MONTH] + 1, endOfQuarter()[Calendar.MONTH])
}

fun Calendar.getDaysInQuarter(): Int {
    val cal = Calendar.getInstance()
    cal.time = this.time
    cal.set(Calendar.DAY_OF_MONTH, 1)
    var result = 0
    getMonthsOfQuarter().forEach {
        cal.set(Calendar.MONTH, it)
        result += cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    }
    return result
}


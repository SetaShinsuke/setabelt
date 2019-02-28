package com.seta.common.extensions

/**
 * Created by SETA_WORK on 2017/7/18.
 * Collections 的扩展函数
 */


/**
 * Map -> (List) -> vararg 数组
 */
fun <K, V : Any> MutableMap<K, V?>.toVarargArray(): Array<out Pair<K, V?>> =
        map({ Pair(it.key, it.value) }).toTypedArray()

fun <T> MutableCollection<T>.switch(element: T) {
    if (contains(element)) {
        remove(element)
    } else {
        add(element)
    }
}

/**
 * 检查Database中读取出的类型
 * 如果是【小于 Int 最大值的Long】,则当作【Int】来处理
 * 如果是【Unit】,则当作【null】对象
 */
fun <K, V> Map<K, V>.varyByDb(): Map<K, Any?>? = mapValues {
    if (it.value is Long && (it.value as Long) < Int.MAX_VALUE) {
        return@mapValues (it.value as Long).toInt()
    } else if (it.value is Unit) {
        return@mapValues null
    } else {
        return@mapValues it.value
    }
}
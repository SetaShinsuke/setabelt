package com.seta.common.extensions

import android.database.sqlite.SQLiteDatabase
import com.seta.common.logs.LogX
import org.jetbrains.anko.db.*

/**
 * Created by SETA_WORK on 2017/7/18.
 */

/**
 * 把 cursor 转换成 collection
 */
fun <T : Any> SelectQueryBuilder.parseList(parser: (Map<String, Any?>) -> T): List<T> =
        parseList(object : MapRowParser<T> {
            override fun parseRow(columns: Map<String, Any?>): T = parser(columns)
        })

fun <T : Any> SelectQueryBuilder.parseOpt(parser: (Map<String, Any?>) -> T): T? =
        parseOpt(object : MapRowParser<T> {
            override fun parseRow(columns: Map<String, Any?>): T = parser(columns)
        })


fun SQLiteDatabase.clear(tableName: String) {
    execSQL("delete from $tableName")
}

fun SelectQueryBuilder.byId(id: Long) = whereSimple("_id = ?", id.toString())

fun SQLiteDatabase.exists(tableName: String,
                          argName: String,
                          argValue: String): Boolean {
    var exist = false
    select(tableName)
            .whereSimple("$argName = ?", argValue)
            .parseList(object : MapRowParser<Unit> {
                override fun parseRow(columns: Map<String, Any?>): Unit {
                    exist = columns.isNotEmpty()
                }
            })
    return exist
}

fun SQLiteDatabase.insertOrUpdate(tableName: String,
                                  argName: String,
                                  argValue: String,
                                  vararg values: Pair<String, Any?>) {
    if (exists(tableName, argName, argValue)) {
        val result = update(tableName, *values).exec()
        LogX.d("Update result : $result")
    } else {
        insert(tableName, *values)
    }
}

fun FOREIGN_KEY_CASCADE(columnName: String, referenceTable: String, referenceColumn: String): Pair<String, SqlType> {
    return "" to SqlTypeImpl("FOREIGN KEY($columnName) REFERENCES $referenceTable($referenceColumn) ON DELETE CASCADE ")
}

fun FOREIGN_KEY_CASCADE_MULTI_LINE(constraintName: String, columnName: String, referenceTable: String, referenceColumn: String): Pair<String, SqlType> {
    return "" to SqlTypeImpl("CONSTRAINT $constraintName FOREIGN KEY($columnName) REFERENCES $referenceTable($referenceColumn) ON DELETE CASCADE ")
}

private open class SqlTypeImpl(override val name: String, val modifiers: String? = null) : SqlType {
    override fun render() = if (modifiers == null) name else "$name $modifiers"

    override fun plus(m: SqlTypeModifier): SqlType {
        return SqlTypeImpl(name, if (modifiers == null) m.modifier else "$modifiers $m")
    }
}

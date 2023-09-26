package com.stahlt.cash_flow.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.stahlt.cash_flow.entity.CashEntry

class DatabaseHandler(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dbfile.sqlite"
        private const val DATABASE_VERSION = 2
        private const val TABLE_NAME = "cashFlow"
        private const val KEY_ID = "_id"
        private const val KEY_TYPE = "type"
        private const val KEY_DETAIL = "detail"
        private const val KEY_VALUE = "value"
        private const val KEY_DATE = "date"
        private const val ID_INDEX = 0
        private const val TYPE_INDEX = 1
        private const val DETAIL_INDEX = 2
        private const val VALUE_INDEX = 3
        private const val DATE_INDEX = 4
    }
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS " +
                "$TABLE_NAME " +
                "($KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$KEY_TYPE TEXT, " +
                "$KEY_DETAIL TEXT, " +
                "$KEY_VALUE TEXT, " +
                "$KEY_DATE TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE $TABLE_NAME")
        onCreate(db)
    }

    fun create(cashEntry: CashEntry) {
        val db = writableDatabase
        val register = ContentValues()
        register.put(KEY_TYPE, cashEntry.type)
        register.put(KEY_DETAIL, cashEntry.detail)
        register.put(KEY_VALUE, cashEntry.value)
        register.put(KEY_DATE, cashEntry.date)
        db.insert(TABLE_NAME, null, register)
    }

    fun read(id: Int): CashEntry? {
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, "$KEY_ID=$id",
            null, null, null, null)
        var cashEntry: CashEntry? = null
        cursor.run {
            if (moveToNext()) {
                cashEntry = CashEntry(
                    cursor.getString(TYPE_INDEX),
                    cursor.getString(DETAIL_INDEX),
                    cursor.getString(VALUE_INDEX),
                    cursor.getString(DATE_INDEX))
                close()
            }
        }
        return cashEntry
    }

    fun update(id: Int, cashEntry: CashEntry) {
        val db = writableDatabase
        val register = ContentValues()
        register.put(KEY_TYPE, cashEntry.type)
        register.put(KEY_DETAIL, cashEntry.detail)
        register.put(KEY_VALUE, cashEntry.value)
        register.put(KEY_DATE, cashEntry.date)
        db.update(TABLE_NAME, register, "$KEY_ID=$id", null)
    }

    fun delete(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$KEY_ID=$id", null)
    }

    fun listCursor(): Cursor {
       val db = readableDatabase
       return db.query(TABLE_NAME, null, null, null, null,
           null, null)
    }

    fun getAllPrimaryKeys(): ArrayList<Int> {
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null,
            null, null, null)
        var pks = ArrayList<Int>()
        cursor.run {
            while(moveToNext()) {
                pks.add(cursor.getInt(ID_INDEX))
            }
            close()
        }
        return pks
    }

    fun getBalance(): Double {
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        var balance = 0.0
        while(cursor.moveToNext()) {
            val type = cursor.getString(TYPE_INDEX)
            if (type == "Credit") {
                val stringValue = cursor.getString(VALUE_INDEX)
                balance += stringValue.toDouble()
            } else if (type == "Debit") {
                balance -= cursor.getString(VALUE_INDEX).toDouble()
            }
        }
        cursor.close()
        return balance
    }
}
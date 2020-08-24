package com.example.list

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MemoOpenHelper
    (context: Context) : SQLiteOpenHelper(context, DBName, null, VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE MEMO_TABLE (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "uuid TEXT, " +
                    "body TEXT)"
        )

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS MEMO_TABLE")

        onCreate(db)
    }

    companion object {

        private const val DBName = "MEMO_DB"

        private const val VERSION = 1
    }
}
package com.magnum_ti1.plant.data.local.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.magnum_ti1.plant.common.Constant.BIO
import com.magnum_ti1.plant.common.Constant.DB_NAME
import com.magnum_ti1.plant.common.Constant.DB_VERSION
import com.magnum_ti1.plant.common.Constant.ID
import com.magnum_ti1.plant.common.Constant.TABLE_NAME

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(
            "CREATE TABLE $TABLE_NAME (ID INTEGER PRIMARY KEY " +
                    "AUTOINCREMENT,BIO TEXT)"
        )
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(p0)
    }

    fun createData(dataBio: String?) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(BIO, dataBio)
        db.insert(TABLE_NAME, null, contentValues)
    }

    fun readData(id: String?): Cursor {
        val db = this.writableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME WHERE ID = $id", null)
    }

    fun updateData(id: String?, dataBio: String?): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, id)
        contentValues.put(BIO, dataBio)
        db.update(TABLE_NAME, contentValues, "ID = ?", arrayOf(id))
        return true
    }

    fun deleteData(id: String?): Boolean {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "ID = ?", arrayOf(id))
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '$TABLE_NAME'")
        return true
    }
}
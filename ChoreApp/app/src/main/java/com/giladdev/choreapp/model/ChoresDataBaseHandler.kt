package com.giladdev.choreapp.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.text.DateFormat
import java.util.*

class ChoresDataBaseHandler(context : Context) :
    SQLiteOpenHelper(context, DATABASE_NAME,null,DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase?) {
            // SQL - structured query language
            var CREATE_CHORE_TABLE = "CREATE TABLE "+ TABLE_NAME+"("+ KEY_ID+" INTEGER PRIMARY KEY,"+
                                     KEY_CHORE_NAME+" TEXT," +
                                     KEY_CHORE_ASSIGNED_BY+" TEXT,"+
                                     KEY_CHORE_ASSIGNED_TO+" TEXT,"+
                                     KEY_CHORE_ASSIGNED_TIME+" LONG"+")"

            db?.execSQL(CREATE_CHORE_TABLE)


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXSITS "+ TABLE_NAME)
        onCreate(db)
    }

    /**
     * CURD - create , read, update, delete
     */

    fun CreateChore(chore : Chore){
        var db : SQLiteDatabase  = writableDatabase

        var values: ContentValues = ContentValues()

        values.put(KEY_CHORE_NAME,chore.choreName)
        values.put(KEY_CHORE_ASSIGNED_BY,chore.assignedBy)
        values.put(KEY_CHORE_ASSIGNED_TO,chore.assignedTo)
        values.put(KEY_CHORE_ASSIGNED_TIME,System.currentTimeMillis())

        db.insert(TABLE_NAME, null, values)

        Log.d("DATA INSERTED","SUCCESS")

        db.close()
    }

    fun readChores(): ArrayList<Chore> {


        var db: SQLiteDatabase = readableDatabase
        var list: ArrayList<Chore> = ArrayList()

        //Select all chores from table
        var selectAll = "SELECT * FROM " + TABLE_NAME

        var cursor: Cursor = db.rawQuery(selectAll, null)

        //loop through our chores
        if (cursor.moveToFirst()) {
            do {
                var chore = Chore()

                chore.id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                chore.choreName = cursor.getString(cursor.getColumnIndex(KEY_CHORE_NAME))
                chore.assignedTo = cursor.getString(cursor.getColumnIndex(KEY_CHORE_ASSIGNED_TO))
                chore.timerAssigned = cursor.getLong(cursor.getColumnIndex(KEY_CHORE_ASSIGNED_TIME))
                chore.assignedBy = cursor.getString(cursor.getColumnIndex(KEY_CHORE_ASSIGNED_BY))

                list.add(chore)

            }while (cursor.moveToNext())
        }


        return list

    }

    fun ReadChore(id : Int) : Chore{
        var db : SQLiteDatabase  = writableDatabase

        var cursor : Cursor = db.query(TABLE_NAME,arrayOf(KEY_ID, KEY_CHORE_NAME, KEY_CHORE_ASSIGNED_TO, KEY_CHORE_ASSIGNED_BY,
                                       KEY_CHORE_ASSIGNED_TIME),KEY_ID+"=?", arrayOf(id.toString())
                                       ,null,null,null,null)

        if (cursor != null){
            cursor.moveToFirst()
        }
        var chore = Chore()

        chore.id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
        chore.choreName = cursor.getString(cursor.getColumnIndex(KEY_CHORE_NAME))
        chore.assignedBy = cursor.getString(cursor.getColumnIndex(KEY_CHORE_ASSIGNED_BY))
        chore.assignedTo = cursor.getString(cursor.getColumnIndex(KEY_CHORE_ASSIGNED_TO))
        chore.timerAssigned = cursor.getLong(cursor.getColumnIndex(KEY_CHORE_ASSIGNED_TIME))

        var dateFormat : java.text.DateFormat = DateFormat.getDateInstance()
        var formattedDate = dateFormat.format(Date(chore.timerAssigned).time) // feb 24, 2017 ( something like that)

        return chore
    }

    fun UpdateChore(chore : Chore) : Int {
        var db : SQLiteDatabase  = writableDatabase

        var values: ContentValues = ContentValues()


        values.put(KEY_CHORE_NAME,chore.choreName)
        values.put(KEY_CHORE_ASSIGNED_BY,chore.assignedBy)
        values.put(KEY_CHORE_ASSIGNED_TO,chore.assignedTo)
        values.put(KEY_CHORE_ASSIGNED_TIME,System.currentTimeMillis())

        // return the updated row
        return db.update(TABLE_NAME, values, KEY_ID + "=?", arrayOf(chore.id.toString()))
    }

    fun deleteChore(id : Int)
    {
        var db : SQLiteDatabase  = writableDatabase

        db.delete(TABLE_NAME,KEY_ID+"=?",arrayOf(id.toString()))

        db.close()
    }

    fun GetChoresCount() : Int{
        var db : SQLiteDatabase  = readableDatabase
        var countQuery = "SELECT * FROM "+ TABLE_NAME
        var cursor : Cursor = db.rawQuery(countQuery,null)

        return cursor.count
    }


}
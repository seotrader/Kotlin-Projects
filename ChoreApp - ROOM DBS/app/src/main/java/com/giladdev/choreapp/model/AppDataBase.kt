package com.giladdev.choreapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ChoresEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract  fun ChoresDao(): ChoresDao

    companion object{
        var INSTANCE : AppDataBase? = null

        fun getAppDataBase(context: Context) : AppDataBase? {

            if (INSTANCE == null) {
                synchronized(AppDataBase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java,
                        "ChoresTest.DB" ).build()

                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }

    }
}
package com.giladdev.rickyandmarty.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CharecterDBEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase()  {
    abstract fun CharactersDAO(): CharactersDAO
    companion object{
        var INSTANCE : AppDataBase? = null

        fun getAppDataBase(context: Context) : AppDataBase? {

            if (INSTANCE == null) {
                synchronized(AppDataBase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java,
                        "Characters.DB" ).build()

                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }

    }
}
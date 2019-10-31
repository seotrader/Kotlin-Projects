package com.giladdev.choreapp.model

import androidx.room.*

@Dao

interface ChoresDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChore(chore: ChoresEntity)

    @Update
    fun updateChore(chore: ChoresEntity)

    @Delete
    fun deleteChore(chore: ChoresEntity)

    @Query("SELECT * FROM chores")
    fun getChores(): List<ChoresEntity>
}
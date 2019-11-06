package com.giladdev.rickyandmarty.model
import androidx.room.*

@Dao

interface CharactersDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChore(chore: CharecterDBEntity)

    @Update
    fun updateChore(chore: CharecterDBEntity)

    @Delete
    fun deleteChore(chore: CharecterDBEntity)

    @Query("SELECT * FROM characters")
    fun getChores(): List<CharecterDBEntity>
}
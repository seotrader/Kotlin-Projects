package com.giladdev.rickyandmarty.model
import androidx.room.*

@Dao

interface CharactersDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharecters(chore: CharecterDBEntity)

    @Update
    fun updateCharecters(chore: CharecterDBEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(choreList: List<CharecterDBEntity>)

    @Delete
    fun deleteChore(chore: CharecterDBEntity)

    @Query ("DELETE FROM characters")
    fun deleteAll()

    @Query("SELECT * FROM characters")
    fun getCharacters(): List<CharecterDBEntity>
}
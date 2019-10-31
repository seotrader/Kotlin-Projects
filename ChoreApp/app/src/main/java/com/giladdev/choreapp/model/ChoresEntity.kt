package com.giladdev.choreapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "chores")
data class ChoresEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var choreName: String,
    var assignedBy: String,
    var assignedTo: String,
    val timerAssigned: Long)


package com.anushka.viewmodelscopedemo

import kotlinx.coroutines.delay

class UserRepository {
    suspend fun getUsers(): List<User> {
        delay(8000)
        val users: List<User> = listOf(
            User(1, "Sam"),
            User(2, "Zaki"),
            User(3, "buoko"),
            User(4, "paul")
        )
        return users
    }
    
}
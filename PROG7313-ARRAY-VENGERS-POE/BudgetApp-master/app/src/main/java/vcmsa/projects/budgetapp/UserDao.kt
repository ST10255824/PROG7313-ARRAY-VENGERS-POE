package com.example.budgetapp

import androidx.room.*


//Code attribution
//Title: Save data in a local database using Room
//Author: AndroidDevelopers
//Date accessed: 15 April 2026
//Version: 1
//Availability: https://developer.android.com/training/data-storage/room#kotlin
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): User?

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?
}

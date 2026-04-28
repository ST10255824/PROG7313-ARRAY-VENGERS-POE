package com.example.budgetapp

import androidx.room.*


//Code attribution
//Title: Save data in a local database using Room
//Author: AndroidDevelopers
//Date accessed: 15 April 2026
//Version: 1
//Availability: https://developer.android.com/training/data-storage/room#kotlin
@Dao
interface MonthlyGoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: MonthlyGoal)

    @Query("SELECT * FROM monthly_goals WHERE month = :month LIMIT 1")
    suspend fun getGoalForMonth(month: String): MonthlyGoal?
}

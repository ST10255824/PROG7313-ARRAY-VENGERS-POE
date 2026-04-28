package com.example.budgetapp

import androidx.room.Entity
import androidx.room.PrimaryKey
//Code attribution
//Title: Save data in a local database using Room
//Author: AndroidDevelopers
//Date accessed: 15 April 2026
//Version: 1
//Availability: https://developer.android.com/training/data-storage/room#kotlin
@Entity(tableName = "monthly_goals")
data class MonthlyGoal(
    @PrimaryKey val month: String, // Format: "2026-04"
    val minGoal: Double,
    val maxGoal: Double
)

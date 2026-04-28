package com.example.budgetapp

import androidx.room.Entity
import androidx.room.PrimaryKey
//Code attribution
//Title: Save data in a local database using Room
//Author: AndroidDevelopers
//Date accessed: 15 April 2026
//Version: 1
//Availability: https://developer.android.com/training/data-storage/room#kotlin
@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val description: String,
    val amount: Double,
    val date: String,
    val startTime: String,
    val endTime: String,
    val categoryId: String,
    val photoUri: String? = null,
    val userId: Int // Track which user created this expense
)

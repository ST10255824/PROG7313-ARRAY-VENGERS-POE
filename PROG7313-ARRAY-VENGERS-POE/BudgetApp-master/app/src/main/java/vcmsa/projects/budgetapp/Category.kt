package com.example.budgetapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


//Code attribution
//Title: Save data in a local database using Room
//Author: AndroidDevelopers
//Date accessed: 15 April 2026
//Version: 1
//Availability: https://developer.android.com/training/data-storage/room#kotlin
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    val userId: Int // Associate category with a user
)

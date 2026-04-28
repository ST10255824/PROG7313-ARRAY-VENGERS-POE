package com.example.budgetapp

import androidx.room.*

data class CategoryTotal(
    val categoryId: String,
    val totalAmount: Double
)

//Code attribution
//Title: Save data in a local database using Room
//Author: AndroidDevelopers
//Date accessed: 15 April 2026
//Version: 1
//Availability: https://developer.android.com/training/data-storage/room#kotlin

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: Expense)

    @Query("SELECT * FROM expenses WHERE userId = :userId")
    suspend fun getAllForUser(userId: Int): List<Expense>

    @Query("SELECT * FROM expenses WHERE userId = :userId AND date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    suspend fun getExpensesBetweenDates(userId: Int, startDate: String, endDate: String): List<Expense>

    @Query("SELECT SUM(amount) FROM expenses WHERE userId = :userId AND date LIKE :month || '%'")
    suspend fun getTotalSpentForMonth(userId: Int, month: String): Double?

    @Query("SELECT categoryId, SUM(amount) as totalAmount FROM expenses WHERE userId = :userId AND date BETWEEN :startDate AND :endDate GROUP BY categoryId")
    suspend fun getCategoryTotalsBetweenDates(userId: Int, startDate: String, endDate: String): List<CategoryTotal>
}

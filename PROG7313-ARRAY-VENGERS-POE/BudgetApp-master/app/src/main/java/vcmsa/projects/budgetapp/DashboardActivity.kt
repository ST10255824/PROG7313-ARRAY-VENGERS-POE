package com.example.budgetapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DashboardActivity : AppCompatActivity() {

    private lateinit var tvRemainingBudget: TextView
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Initialize database and the budget text view
        db = AppDatabase.getInstance(this)
        tvRemainingBudget = findViewById(R.id.tvRemainingBudget)

        // Navigation Listeners
        findViewById<MaterialButton>(R.id.btnExpenseEntry).setOnClickListener {
            startActivity(Intent(this, ExpenseEntryActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.btnViewExpenses).setOnClickListener {
            startActivity(Intent(this, ExpenseListActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.btnCategoryReport).setOnClickListener {
            startActivity(Intent(this, CategoryReportActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.btnCategory).setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.btnBudgetGoal).setOnClickListener {
            startActivity(Intent(this, BudgetGoalActivity::class.java))
        }
    }
    //Code attribution
//Title: Getting Started with Room Database in Android using Kotlin
//Author: Hari Moradiya
//Date accessed: 15 April 2026
//Version: 1
//Availability: https://medium.com/@harimoradiya/getting-started-with-room-database-in-android-using-kotlin-92f84b6a5e6c

    override fun onResume() {
        super.onResume()
        refreshBudget()
    }
    //Code attribution
//Title: Accessing data using Room DAOs
//Author: AndroidDevelopers
//Date accessed: 15 April 2026
//Version: 1
//Availability: https://developer.android.com/training/data-storage/room/accessing-data

    private fun refreshBudget() {
        //  Get current month in YYYY-MM format
        val currentMonth = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date())
        val userId = 1

        lifecycleScope.launch {
            //  Fetch the budget goal for this specific month
            val goal = db.monthlyGoalDao().getGoalForMonth(currentMonth)

            //  Fetch total spent from the expenses table
            val totalSpent = db.expenseDao().getTotalSpentForMonth(userId, currentMonth) ?: 0.0

            runOnUiThread {
                // 4. Update the Dashboard Text
                if (goal != null) {
                    val remaining = goal.maxGoal - totalSpent

                    // Format to 2 decimal places
                    tvRemainingBudget.text = "R ${String.format("%.2f", remaining)}"

                    // 5. Visual Feedback: Red if overspent, White if safe
                    if (remaining < 0) {
                        tvRemainingBudget.setTextColor(resources.getColor(android.R.color.holo_red_light))
                    } else {
                        tvRemainingBudget.setTextColor(resources.getColor(android.R.color.white))
                    }
                } else {
                    // If no budget is found in the database for this month
                    tvRemainingBudget.text = "Set Goal"
                    tvRemainingBudget.setTextColor(resources.getColor(android.R.color.white))
                }
            }
        }
    }
}

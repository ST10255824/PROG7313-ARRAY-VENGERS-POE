package com.example.budgetapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class BudgetGoalActivity : AppCompatActivity() {
    private lateinit var monthEditText: EditText
    private lateinit var minGoalEditText: EditText
    private lateinit var maxGoalEditText: EditText
    private lateinit var saveGoalButton: Button
    private lateinit var tvCurrentLimitLabel: TextView
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_goal)

        // Initialize Database and Views
        db = AppDatabase.getInstance(this)
        monthEditText = findViewById(R.id.monthEditText)
        minGoalEditText = findViewById(R.id.minGoalEditText)
        maxGoalEditText = findViewById(R.id.maxGoalEditText)
        saveGoalButton = findViewById(R.id.saveGoalButton)
        tvCurrentLimitLabel = findViewById(R.id.tvCurrentLimitLabel)


        val currentMonth = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date())
        monthEditText.setText(currentMonth)

//Code attribution
//Title: Accessing data using Room DAOs
//Author: AndroidDevelopers
//Date accessed: 15 April 2026
//Version: 1
//Availability: https://developer.android.com/training/data-storage/room/accessing-data
        // 2. Fetch existing data for this month if it exists
        lifecycleScope.launch {
            val existingGoal = db.monthlyGoalDao().getGoalForMonth(currentMonth)
            runOnUiThread {
                existingGoal?.let {
                    tvCurrentLimitLabel.text = "R ${String.format("%.2f", it.maxGoal)}"
                    minGoalEditText.setText(it.minGoal.toString())
                    maxGoalEditText.setText(it.maxGoal.toString())
                }
            }
        }

        saveGoalButton.setOnClickListener {
            val month = monthEditText.text.toString().trim()
            val minText = minGoalEditText.text.toString().trim()
            val maxText = maxGoalEditText.text.toString().trim()

            val min = minText.toDoubleOrNull()
            val max = maxText.toDoubleOrNull()

            // 3. Validation Check
            if (month.isNotEmpty() && min != null && max != null) {
                if (min > max) {
                    Toast.makeText(this, "Minimum cannot be higher than maximum", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val goal = MonthlyGoal(month = month, minGoal = min, maxGoal = max)

                lifecycleScope.launch {
                    db.monthlyGoalDao().insert(goal)
                    runOnUiThread {
                        Toast.makeText(this@BudgetGoalActivity, "Budget for $month Updated!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } else {
                // This triggers if the Month is empty or numbers are invalid
                Toast.makeText(this, "Please enter valid numeric values", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
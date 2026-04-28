package com.example.budgetapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import java.util.*

class CategoryReportActivity : AppCompatActivity() {
    private lateinit var etStartDate: EditText
    private lateinit var etEndDate: EditText
    private lateinit var btnGenerate: Button
    private lateinit var rvTotals: RecyclerView
    private lateinit var adapter: CategoryTotalAdapter
    private lateinit var db: AppDatabase

    //Code attribution
//Title: Getting Started with Room Database in Android using Kotlin
//Author: Hari Moradiya
//Date accessed: 15 April 2026
//Version: 1
//Availability: https://medium.com/@harimoradiya/getting-started-with-room-database-in-android-using-kotlin-92f84b6a5e6c

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_report)

        db = AppDatabase.getInstance(this)
        etStartDate = findViewById(R.id.etReportStartDate)
        etEndDate = findViewById(R.id.etReportEndDate)
        btnGenerate = findViewById(R.id.btnGenerateReport)
        rvTotals = findViewById(R.id.rvCategoryTotals)

        adapter = CategoryTotalAdapter(emptyList())
        rvTotals.layoutManager = LinearLayoutManager(this)
        rvTotals.adapter = adapter

        // Set up Date Picker listeners for the "From" and "To" fields
        etStartDate.setOnClickListener { showDatePicker(etStartDate) }
        etEndDate.setOnClickListener { showDatePicker(etEndDate) }

        btnGenerate.setOnClickListener {
            val startDate = etStartDate.text.toString().trim()
            val endDate = etEndDate.text.toString().trim()

            if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
                loadReport(startDate, endDate)
            } else {
                Toast.makeText(this, "Please select both start and end dates", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Helper function to display the calendar dialog
    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Formats the date as YYYY-MM-DD for database compatibility
                val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                editText.setText(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun loadReport(startDate: String, endDate: String) {
        val userId = UserSession.currentUser?.id ?: return
        lifecycleScope.launch {
            val totals = db.expenseDao().getCategoryTotalsBetweenDates(userId, startDate, endDate)
            runOnUiThread {
                adapter.updateData(totals)
                if (totals.isEmpty()) {
                    Toast.makeText(this@CategoryReportActivity, "No data for this period", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
package com.example.budgetapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class ExpenseListActivity : AppCompatActivity() {
    private lateinit var etStartDate: EditText
    private lateinit var etEndDate: EditText
    private lateinit var btnFilter: Button
    private lateinit var rvExpenses: RecyclerView
    private lateinit var adapter: ExpenseAdapter
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_list)

        db = AppDatabase.getInstance(this)
        etStartDate = findViewById(R.id.etStartDate)
        etEndDate = findViewById(R.id.etEndDate)
        btnFilter = findViewById(R.id.btnFilter)
        rvExpenses = findViewById(R.id.rvExpenses)

        adapter = ExpenseAdapter(emptyList())
        rvExpenses.layoutManager = LinearLayoutManager(this)
        rvExpenses.adapter = adapter

        btnFilter.setOnClickListener {
            val startDate = etStartDate.text.toString().trim()
            val endDate = etEndDate.text.toString().trim()

            if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
                loadExpenses(startDate, endDate)
            } else {
                Toast.makeText(this, "Please enter both start and end dates", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        loadAllExpenses()
    }
    //Code attribution
//Title: Accessing data using Room DAOs
//Author: AndroidDevelopers
//Date accessed: 15 April 2026
//Version: 1
//Availability: https://developer.android.com/training/data-storage/room/accessing-data

    private fun loadExpenses(startDate: String, endDate: String) {
        val userId = UserSession.currentUser?.id ?: return
        lifecycleScope.launch {
            val expenses = db.expenseDao().getExpensesBetweenDates(userId, startDate, endDate)
            runOnUiThread {
                adapter.updateData(expenses)
                if (expenses.isEmpty()) {
                    Toast.makeText(this@ExpenseListActivity, "No expenses found for this period", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadAllExpenses() {
        val userId = UserSession.currentUser?.id ?: return
        lifecycleScope.launch {
            val expenses = db.expenseDao().getAllForUser(userId)
            runOnUiThread {
                adapter.updateData(expenses)
            }
        }
    }
}
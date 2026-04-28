package com.example.budgetapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class CategoryActivity : AppCompatActivity() {
    private lateinit var categoryNameEditText: EditText
    private lateinit var createCategoryButton: Button

    //Code attribution
//Title: Getting Started with Room Database in Android using Kotlin
//Author: Hari Moradiya
//Date accessed: 15 April 2026
//Version: 1
//Availability: https://medium.com/@harimoradiya/getting-started-with-room-database-in-android-using-kotlin-92f84b6a5e6c

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        categoryNameEditText = findViewById(R.id.categoryNameEditText)
        createCategoryButton = findViewById(R.id.createCategoryButton)

        val currentUser = UserSession.currentUser
        if (currentUser == null) {
            finish()
            return
        }

        createCategoryButton.setOnClickListener {
            val categoryName = categoryNameEditText.text.toString().trim()

            if (categoryName.isNotEmpty()) {
                val category = Category(name = categoryName, userId = currentUser.id)
                
                lifecycleScope.launch {
                    AppDatabase.getInstance(applicationContext).categoryDao().insert(category)
                    runOnUiThread {
                        Toast.makeText(this@CategoryActivity, "Category '$categoryName' created", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Please enter a category name", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

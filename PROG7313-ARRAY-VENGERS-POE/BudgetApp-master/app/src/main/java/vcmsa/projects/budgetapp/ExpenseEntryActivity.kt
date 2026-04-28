package com.example.budgetapp

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.*

class ExpenseEntryActivity : AppCompatActivity() {
    private lateinit var descriptionEditText: EditText
    private lateinit var amountEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var startTimeEditText: EditText
    private lateinit var endTimeEditText: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var addPhotoButton: Button
    private lateinit var saveButton: Button
    private lateinit var photoPreview: ImageView
    private var photoUri: String? = null
    private lateinit var db: AppDatabase
    private var categories: List<Category> = emptyList()

    //Code attribution
//Title: Getting Started with Room Database in Android using Kotlin
//Author: Hari Moradiya
//Date accessed: 15 April 2026
//Version: 1
//Availability: https://medium.com/@harimoradiya/getting-started-with-room-database-in-android-using-kotlin-92f84b6a5e6c
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_entry)

        db = AppDatabase.getInstance(this)
        val currentUser = UserSession.currentUser
        if (currentUser == null) {
            finish()
            return
        }

        descriptionEditText = findViewById(R.id.descriptionEditText)
        amountEditText = findViewById(R.id.amountEditText)
        dateEditText = findViewById(R.id.dateEditText)
        startTimeEditText = findViewById(R.id.startTimeEditText)
        endTimeEditText = findViewById(R.id.endTimeEditText)
        categorySpinner = findViewById(R.id.categorySpinner)
        addPhotoButton = findViewById(R.id.addPhotoButton)
        saveButton = findViewById(R.id.saveButton)
        photoPreview = findViewById(R.id.photoPreview)

        // Date Picker
        dateEditText.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                val formattedDate = String.format("%04d-%02d-%02d", year, month + 1, day)
                dateEditText.setText(formattedDate)
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }
        //Code attribution
//Title: Accessing data using Room DAOs
//Author: AndroidDevelopers
//Date accessed: 15 April 2026
//Version: 1
//Availability: https://developer.android.com/training/data-storage/room/accessing-data

        // Load Categories into Spinner
        lifecycleScope.launch {
            categories = db.categoryDao().getAllForUser(currentUser.id)
            val categoryNames = categories.map { it.name }
            val adapter = ArrayAdapter(this@ExpenseEntryActivity, android.R.layout.simple_spinner_item, categoryNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            runOnUiThread {
                categorySpinner.adapter = adapter
            }
        }
        //Code attribution
//Title: Photo picker
//Author: AndroidDevelopers
//Date accessed: 15 April 2026
//Version: 1
//Availability: https://developer.android.com/training/data-storage/shared/photo-picker
        addPhotoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
                addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivityForResult(intent, 100)
        }

        saveButton.setOnClickListener {
            val description = descriptionEditText.text.toString().trim()
            val amountStr = amountEditText.text.toString().trim()
            val date = dateEditText.text.toString().trim()
            val startTime = startTimeEditText.text.toString().trim()
            val endTime = endTimeEditText.text.toString().trim()
            val selectedCategoryName = categorySpinner.selectedItem?.toString()

            val amount = amountStr.toDoubleOrNull()

            if (description.isNotEmpty() && amount != null && date.isNotEmpty() && selectedCategoryName != null) {
                val expense = Expense(
                    description = description,
                    amount = amount,
                    date = date,
                    startTime = startTime,
                    endTime = endTime,
                    categoryId = selectedCategoryName,
                    photoUri = photoUri,
                    userId = currentUser.id
                )

                lifecycleScope.launch {
                    db.expenseDao().insert(expense)
                    runOnUiThread {
                        Toast.makeText(this@ExpenseEntryActivity, "Expense saved!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //  Handle the result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val selectedUri = data?.data
            if (selectedUri != null) {
                try {

                    val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    contentResolver.takePersistableUriPermission(selectedUri, takeFlags)

                    photoUri = selectedUri.toString()
                    photoPreview.setImageURI(selectedUri)
                } catch (e: Exception) {

                    photoUri = selectedUri.toString()
                    photoPreview.setImageURI(selectedUri)
                }
            }
        }
    }
}
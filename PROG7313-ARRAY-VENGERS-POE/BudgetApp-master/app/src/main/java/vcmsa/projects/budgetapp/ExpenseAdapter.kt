package com.example.budgetapp

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter(private var expenses: List<Expense>) :
    RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPhoto: ImageView = view.findViewById(R.id.ivExpensePhoto)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
        val tvDateTime: TextView = view.findViewById(R.id.tvDateTime)
        val tvAmount: TextView = view.findViewById(R.id.tvAmount) // Added for the new XML field
    }
    //Code attribution
//Title: Getting Started with Room Database in Android using Kotlin
//Author: Hari Moradiya
//Date accessed: 15 April 2026
//Version: 1
//Availability: https://medium.com/@harimoradiya/getting-started-with-room-database-in-android-using-kotlin-92f84b6a5e6c
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]


        holder.tvDescription.text = expense.description
        holder.tvCategory.text = expense.categoryId
        holder.tvDateTime.text = "${expense.date} | ${expense.startTime} - ${expense.endTime}"

        // Bind and format the amount (R 0.00 style)
        holder.tvAmount.text = "R ${String.format("%.2f", expense.amount)}"

        //Code attribution
//Title: Photo picker
//Author: AndroidDevelopers
//Date accessed: 15 April 2026
//Version: 1
//Availability: https://developer.android.com/training/data-storage/shared/photo-picker
        // Handle image loading and permissions
        if (!expense.photoUri.isNullOrEmpty()) {
            try {
                val uri = Uri.parse(expense.photoUri)
                holder.ivPhoto.setImageURI(uri)

                // Allow user to click the photo to view it in full screen
                holder.ivPhoto.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(uri, "image/*")
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    holder.itemView.context.startActivity(intent)
                }
            } catch (e: Exception) {

                holder.ivPhoto.setImageResource(android.R.drawable.ic_menu_report_image)
            }
        } else {
            // Placeholder if no photo exists
            holder.ivPhoto.setImageResource(android.R.color.darker_gray)
            holder.ivPhoto.setOnClickListener(null)
        }
    }

    override fun getItemCount() = expenses.size

    fun updateData(newExpenses: List<Expense>) {
        expenses = newExpenses
        notifyDataSetChanged()
    }
}

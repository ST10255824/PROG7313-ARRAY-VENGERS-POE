package com.example.budgetapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView



//Code attribution
//Title: Create dynamic lists with RecyclerView
//Author: AndroidDevelopers
//Date accessed: 15 April 2026
//Version: 1
//Availability: https://developer.android.com/training/data-storage/room#kotlin

class CategoryTotalAdapter(private var totals: List<CategoryTotal>) :
    RecyclerView.Adapter<CategoryTotalAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCategoryName: TextView = view.findViewById(R.id.tvCategoryName)
        val tvTotalAmount: TextView = view.findViewById(R.id.tvTotalAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_total, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val total = totals[position]
        holder.tvCategoryName.text = total.categoryId
        holder.tvTotalAmount.text = String.format("R %.2f", total.totalAmount)
    }

    override fun getItemCount() = totals.size

    fun updateData(newTotals: List<CategoryTotal>) {
        totals = newTotals
        notifyDataSetChanged()
    }
}

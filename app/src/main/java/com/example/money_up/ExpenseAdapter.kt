package com.example.money_up

import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import data.ExpenseTable.Expense


class ExpenseAdapter : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    private var expenses = listOf<Expense>()

    fun submitList(list: List<Expense>) {
        expenses = list
        notifyDataSetChanged()
    }

    inner class ExpenseViewHolder(itemView: TextView) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val textView = TextView(parent.context)
        return ExpenseViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        holder.textView.text = "${expense.expenseName} - ${expense.amount} - ${expense.date}"

        if (expense.photo.isNotEmpty()) {
            holder.textView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(Uri.parse(expense.photo), "image/*")
                holder.textView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = expenses.size
}

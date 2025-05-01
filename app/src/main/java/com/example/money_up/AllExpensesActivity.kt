package com.example.money_up

import ExpenseAdapter
import android.app.ActivityOptions
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import data.MoneyUpDatabase
import data.ExpenseTable.ExpenseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


class AllExpensesActivity : AppCompatActivity() {

    private lateinit var pickDateButton: Button
    private lateinit var applyFilterButton: Button
    private lateinit var clearFiltersButton: Button
    private lateinit var expensesRecyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter
    private lateinit var expenseDao: ExpenseDao

    private var startDate: String? = null
    private var endDate: String? = null

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_expenses)

        // Initialize DAO first
        expenseDao = MoneyUpDatabase.getDatabase(this).expenseDao()





        // Expense list initialization
        pickDateButton = findViewById(R.id.btn_pick_date)
        applyFilterButton = findViewById(R.id.btn_filter)
        clearFiltersButton = findViewById(R.id.clear_filters_button)
        expensesRecyclerView = findViewById(R.id.recycler_expenses)

        expenseAdapter = ExpenseAdapter()
        expensesRecyclerView.adapter = expenseAdapter
        expensesRecyclerView.layoutManager = LinearLayoutManager(this)

        pickDateButton.setOnClickListener { openDateRangePicker() }
        applyFilterButton.setOnClickListener { loadExpenses() }
        clearFiltersButton.setOnClickListener { clearFilters() }

        // Add Expense Button
        val addExpenseButton = findViewById<Button>(R.id.add_expenseBTN)
        addExpenseButton.setOnClickListener {
            val intent = Intent(this, ExpenseActivity::class.java)
            val options = ActivityOptions.makeCustomAnimation(this, 0, 0)
            startActivity(intent, options.toBundle())
        }

        // Bottom Navigation Setup
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.nav_expenses
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomePageActivity::class.java),
                        ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
                    true
                }
                R.id.nav_budget -> {
                    startActivity(Intent(this, BudgetActivity::class.java),
                        ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java),
                        ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
                    true
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingActivity::class.java),
                        ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
                    true
                }
                else -> false
            }
        }
    }

    private fun openDateRangePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val pickedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            if (startDate == null) {
                startDate = pickedDate
                Toast.makeText(this, "Start Date Selected: $startDate", Toast.LENGTH_SHORT).show()
            } else {
                endDate = pickedDate
                Toast.makeText(this, "End Date Selected: $startDate to $endDate", Toast.LENGTH_SHORT).show()
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun loadExpenses() {
        lifecycleScope.launch {
            expenseDao.getAllExpenses().collectLatest { expenses ->
                val filteredExpenses = if (startDate != null && endDate != null) {
                    expenses.filter { isWithinSelectedRange(it.date) }
                } else {
                    expenses
                }
                withContext(Dispatchers.Main) {
                    expenseAdapter.submitList(filteredExpenses)
                }
            }
        }
    }


    private fun clearFilters() {
        startDate = null
        endDate = null
        Toast.makeText(this, "Filters cleared", Toast.LENGTH_SHORT).show()
        loadExpenses()
    }

    private fun isWithinSelectedRange(dateString: String): Boolean {
        if (startDate == null && endDate == null) return true
        val date = dateFormatter.parse(dateString) ?: return false
        val start = dateFormatter.parse(startDate!!) ?: return false
        val end = dateFormatter.parse(endDate!!) ?: return false
        return (date == start || date == end || (date.after(start) && date.before(end)))
    }
}


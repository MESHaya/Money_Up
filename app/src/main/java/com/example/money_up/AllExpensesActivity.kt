package com.example.money_up

import android.app.ActivityOptions
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import data.MoneyUpDatabase
import data.ExpenseTable.Expense
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AllExpensesActivity : AppCompatActivity() {

    private lateinit var pickDateButton: Button
    private lateinit var applyFilterButton: Button
    private lateinit var clearFiltersButton: Button
    private lateinit var expensesRecyclerView: RecyclerView
    private lateinit var categoryTotalsRecyclerView: RecyclerView


    private var startDate: String? = null
    private var endDate: String? = null

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private lateinit var expenseDao: data.ExpenseTable.ExpenseDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_expenses)

        expenseDao = data.MoneyUpDatabase.getDatabase(this).expenseDao()


        pickDateButton = findViewById(R.id.btn_pick_date)
        applyFilterButton = findViewById(R.id.btn_filter)
        clearFiltersButton = findViewById(R.id.clear_filters_button)
        expensesRecyclerView = findViewById(R.id.recycler_expenses)
        categoryTotalsRecyclerView = findViewById(R.id.recycler_category_totals)



        pickDateButton.setOnClickListener { openDateRangePicker() }
        applyFilterButton.setOnClickListener { loadExpenses() }
        clearFiltersButton.setOnClickListener { clearFilters() }

        val addExpenseButton = findViewById<Button>(R.id.add_expenseBTN)
        addExpenseButton.setOnClickListener {
            val intent = Intent(this, ExpenseActivity::class.java)
            val options = ActivityOptions.makeCustomAnimation(this, 0, 0)
            startActivity(intent, options.toBundle())
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.nav_expenses

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_expenses -> true
                R.id.nav_home -> {
                    val intent = Intent(this, HomePageActivity::class.java)
                    val options = ActivityOptions.makeCustomAnimation(this, 0, 0)
                    startActivity(intent, options.toBundle())
                    true
                }
                R.id.nav_budget -> {
                    val intent = Intent(this, BudgetActivity::class.java)
                    val options = ActivityOptions.makeCustomAnimation(this, 0, 0)
                    startActivity(intent, options.toBundle())
                    true
                }
                R.id.nav_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    val options = ActivityOptions.makeCustomAnimation(this, 0, 0)
                    startActivity(intent, options.toBundle())
                    true
                }
                R.id.nav_settings -> {
                    val intent = Intent(this, SettingActivity::class.java)
                    val options = ActivityOptions.makeCustomAnimation(this, 0, 0)
                    startActivity(intent, options.toBundle())
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
                val filteredExpenses = expenses.filter { expense ->
                    isWithinSelectedRange(expense.date)
                }
                displayExpenses(filteredExpenses)
                displayCategoryTotals(filteredExpenses)
            }
        }
    }

    private fun isWithinSelectedRange(dateString: String): Boolean {
        if (startDate == null || endDate == null) return true
        val date = dateFormatter.parse(dateString)
        val start = dateFormatter.parse(startDate!!)
        val end = dateFormatter.parse(endDate!!)
        return (date == start || date == end || (date!!.after(start) && date.before(end)))
    }

    private fun displayExpenses(expenses: List<Expense>) {
        expensesRecyclerView.removeAllViews()

        for (expense in expenses) {
            val textView = TextView(this)
            textView.text = "${expense.expenseName} - ${expense.amount} - ${expense.date}"

            if (expense.photo.isNotEmpty()) {
                textView.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(Uri.parse(expense.photo), "image/*")
                    startActivity(intent)
                }
            }

            expensesRecyclerView.addView(textView)
        }
    }

    private fun displayCategoryTotals(expenses: List<Expense>) {
        categoryTotalsRecyclerView.removeAllViews()

        val categoryTotals = expenses.groupBy { it.category_id }
            .mapValues { entry -> entry.value.sumOf { it.amount } }

        for ((categoryId, totalAmount) in categoryTotals) {
            val textView = TextView(this)
            textView.text = "Category ID $categoryId: $totalAmount"
            categoryTotalsRecyclerView.addView(textView)
        }
    }

    private fun clearFilters() {
        startDate = null
        endDate = null
        Toast.makeText(this, "Filters cleared", Toast.LENGTH_SHORT).show()
        loadExpenses()
    }
}

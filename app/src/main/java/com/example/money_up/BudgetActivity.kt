package com.example.money_up

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import data.MoneyUpDatabase
import data.MonthlyBudgetTable.MonthlyBudgetRepository
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import data.MonthlyBudgetTable.MonthlyBudget
import data.MonthlyBudgetTable.MonthlyBudgetRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class BudgetActivity : AppCompatActivity() {

    private lateinit var repository: MonthlyBudgetRepository
    private lateinit var etBudgetName: EditText
    private lateinit var etMinBudget: EditText
    private lateinit var etMaxBudget: EditText
    private lateinit var btnSaveBudget: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_budget)

        etBudgetName = findViewById(R.id.et_budget_name)
        etMinBudget = findViewById(R.id.et_min_budget)
        etMaxBudget = findViewById(R.id.et_max_budget)
        btnSaveBudget = findViewById(R.id.btn_save_budget)

        val monthlyBudgetDao = MoneyUpDatabase.getDatabase(applicationContext).monthlyBudgetDao()
        repository = MonthlyBudgetRepositoryImpl(monthlyBudgetDao) // assuming you implemented it correctly

        btnSaveBudget.setOnClickListener {
            saveBudget()
        }

        setupBottomNav()
    }

    private fun saveBudget() {
        val  monthlyBudgetName = etBudgetName.text.toString()
        val minAmount = etMinBudget.text.toString().toDoubleOrNull()
        val maxAmount = etMaxBudget.text.toString().toDoubleOrNull()

        if (monthlyBudgetName.isEmpty() || minAmount == null || maxAmount == null) {
            Toast.makeText(this, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
            return
        }

        if (minAmount > maxAmount) {
            Toast.makeText(this, "Minimum budget cannot be greater than maximum budget", Toast.LENGTH_SHORT).show()
            return
        }

        val newTotal = (minAmount + maxAmount).toInt()

        // Launch a coroutine to handle Room operations
        lifecycleScope.launch {
            // Sum all existing total_budget values
            val allBudgets = repository.getAllBudgetsStream().first() // get the current budgets
            val previousTotal = allBudgets.sumOf { it.total_budget }

            val totalBudgetCombined = previousTotal + newTotal

            val newBudget = MonthlyBudget(
                user_id = 1,
                monthlyBudget_name = monthlyBudgetName,
                month = "April",
                total_budget = newTotal,
                min_amont = minAmount.toInt(),
                max_amount = maxAmount.toInt()
            )

            repository.insertBudget(newBudget)

            Toast.makeText(this@BudgetActivity, "Budget saved successfully", Toast.LENGTH_SHORT).show()

            etBudgetName.text.clear()
            etMinBudget.text.clear()
            etMaxBudget.text.clear()
        }
    }

    private fun setupBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_budget -> true
                R.id.nav_expenses -> {
                    startActivity(Intent(this, AllExpensesActivity::class.java))
                    true
                }
                R.id.nav_home -> {
                    startActivity(Intent(this, HomePageActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingActivity::class.java))
                    true
                }
                else -> false
            }
        }

        bottomNav.selectedItemId = R.id.nav_budget
    }
}

package com.example.money_up

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import data.ExpenseTable.ExpenseDao
import data.MoneyUpDatabase
import data.MonthlyBudgetTable.MonthlyBudget
import data.MonthlyBudgetTable.MonthlyBudgetDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.first


class ProfileActivity : AppCompatActivity() {

    private lateinit var monthlyBudgetDao: MonthlyBudgetDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        // Initialize DAO first
        monthlyBudgetDao = MoneyUpDatabase.getDatabase(this).monthlyBudgetDao()

        lifecycleScope.launch {
            val budgetList: List<MonthlyBudget> = withContext(Dispatchers.IO) {
                MoneyUpDatabase.getDatabase(applicationContext)
                    .monthlyBudgetDao()
                    .getAllBudgets()
                    .first()
            }


            // Example: Display total budget of the first entry
            if (budgetList.isNotEmpty()) {
                val firstBudget = budgetList[0]
                val budgetAmount = firstBudget.total_budget
                val budgetName = firstBudget.monthlyBudget_name
                val budgetLabel = findViewById<TextView>(R.id.active_budgets_label)
                budgetLabel.text = "Active Budget: $budgetName - R$budgetAmount"

            }
        }


        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

            bottomNav.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.nav_profile -> true
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
                    R.id.nav_expenses -> {
                        val intent = Intent(this, AllExpensesActivity::class.java)
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

            // Highlight current tab
            bottomNav.selectedItemId = R.id.nav_profile
        }
    }


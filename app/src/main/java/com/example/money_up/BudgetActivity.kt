package com.example.money_up

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import data.MoneyUpDatabase
import data.MonthlyBudgetTable.MonthlyBudgetRepository
import data.UserTable.OfflineUsersRepository
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.money_up.R
import data.MonthlyBudgetTable.MonthlyBudget


class BudgetActivity : AppCompatActivity() {

    //this repo handles the communication with the database
    private lateinit var repository: MonthlyBudgetRepository
    private lateinit var etBudgetName: EditText
    private lateinit var etMinBudget: EditText
    private lateinit var etMaxBudget: EditText
    private lateinit var btnSaveBudget: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_budget)

        // Initialize views
        etBudgetName = findViewById(R.id.et_budget_name)
        etMinBudget = findViewById(R.id.et_min_budget)
        etMaxBudget = findViewById(R.id.et_max_budget)
        btnSaveBudget = findViewById(R.id.btn_save_budget)

        // Get instance of the dao from db
        val monthlyBudgetDao = MoneyUpDatabase.getDatabase(applicationContext).monthlyBudgetDao()
        repository = MonthlyBudgetRepository(monthlyBudgetDao)

        btnSaveBudget.setOnClickListener {
            SaveBudget()
        }

        fun saveBudget() {
            val monthlyBudget_name = etBudgetName.text.toString()
            val min_amount = etMinBudget.text.toString().toDoubleOrNull()
            val max_amount = etMaxBudget.text.toString().toDoubleOrNull()
            //val total_budget=

            if (monthlyBudget_name.isEmpty() || min_amount == null || max_amount  == null) {
                Toast.makeText(this, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
                return
            }

            if (min_amount > max_amount ) {
                Toast.makeText(this, "Minimum budget cannot be greater than maximum budget", Toast.LENGTH_SHORT).show()
                return
            }

            // Create a new MonthlyBudget object
            val newBudget = MonthlyBudget(monthlyBudget_name,min_amount, max_amount )

            // Save the budget to the database
            repository.insert(newBudget)

            Toast.makeText(this, "Budget saved successfully", Toast.LENGTH_SHORT).show()

            // Optionally, clear the input fields
            etBudgetName.text.clear()
            etMinBudget.text.clear()
            etMaxBudget.text.clear()

            // Refresh the RecyclerView or any other UI component to show the updated budget history
            // ...
        }


        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_budget -> true
                R.id.nav_expenses -> {
                    val intent = Intent(this, AllExpensesActivity::class.java)
                    val options = ActivityOptions.makeCustomAnimation(this, 0, 0)
                    startActivity(intent, options.toBundle())
                    true
                }
                R.id.nav_home -> {
                    val intent = Intent(this, HomePageActivity::class.java)
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

        // Highlight current tab
        bottomNav.selectedItemId = R.id.nav_budget
    }
        }

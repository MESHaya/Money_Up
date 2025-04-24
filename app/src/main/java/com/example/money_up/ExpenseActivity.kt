package com.example.money_up

import android.app.ActivityOptions
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class ExpenseActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private var selectedImageUri: Uri? = null

    // Modern image picker launcher
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                imageView.setImageURI(it)
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_expense)

        val uploadPhotoBtn = findViewById<Button>(R.id.upload_photo_button)
        imageView = findViewById(R.id.photo_preview)

        //upload photo functionality
        uploadPhotoBtn.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

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

        // Highlight current tab
        bottomNav.selectedItemId = R.id.nav_expenses
    }
}
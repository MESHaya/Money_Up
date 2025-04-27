package com.example.money_up

import android.app.ActivityOptions
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import data.ExpenseTable.Expense
import data.ExpenseTable.OfflineExpenseRepository
import data.MoneyUpDatabase
import kotlinx.coroutines.CoroutineScope

class ExpenseActivity : AppCompatActivity() {

    private var imageUri: Uri? = null
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            findViewById<ImageView>(R.id.photo_preview).setImageURI(uri)
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_expense)

        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            finish() // Close the current activity and go back to the previous one
        }

        val photoPreview = findViewById<ImageView>(R.id.photo_preview)
        val uploadButton = findViewById<Button>(R.id.upload_photo_button)

        uploadButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

    }


    }

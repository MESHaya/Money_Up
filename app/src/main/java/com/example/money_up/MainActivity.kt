package com.example.money_up

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import data.MoneyUpDatabase
import data.UserTable.UserDao

class MainActivity : AppCompatActivity() {

    private lateinit var db: MoneyUpDatabase
    private val userDao: UserDao by lazy { db.userDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Room Database
        db = Room.databaseBuilder(
            applicationContext,
            MoneyUpDatabase::class.java, "money_up_database"
        ).build()
        setContentView(R.layout.activity_main)


        // Find buttons by their IDs
        val signUpBtn = findViewById<Button>(R.id.Sign_up)
        val loginBtn = findViewById<Button>(R.id.Log_in)

        // Handle Sign Up button click
        signUpBtn.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // Handle Log In button click
        loginBtn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}

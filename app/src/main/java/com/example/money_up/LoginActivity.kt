package com.example.money_up

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import data.MoneyUpDatabase
import data.UserTable.OfflineUsersRepository

class LoginActivity : AppCompatActivity() {

    //this repo handles the communication with the database
    private lateinit var repository: OfflineUsersRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_log_in)


        //get instance of the dao from db
        val userDao = MoneyUpDatabase.getDatabase(applicationContext).userDao()
        //pass dao into repo
        repository = OfflineUsersRepository(userDao)



        val usernameInput = findViewById<EditText>(R.id.username)
        val passwordInput = findViewById<EditText>(R.id.password)
        val remember_me = findViewById<CheckBox>(R.id.remember_me)
        val forgot_pass = findViewById<TextView>(R.id.forgot_password)
        val loginBTN = findViewById<Button>(R.id.log_in_button)
        val signuplink = findViewById<TextView>(R.id.login_signup_link)

        //handle functionality of the login button

        loginBTN.setOnClickListener{
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
        }

        //check if user is in the database



        }
    }

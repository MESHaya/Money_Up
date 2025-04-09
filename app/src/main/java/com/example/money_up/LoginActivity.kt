package com.example.money_up

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Checkable
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_log_in)

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val remember_me = findViewById<CheckBox>(R.id.remember_me)
        val forgot_pass = findViewById<TextView>(R.id.forgot_password)
        val loginBTN = findViewById<Button>(R.id.log_in_button)
        val signuplink = findViewById<TextView>(R.id.login_signup_link)


        }
    }

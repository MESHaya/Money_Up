package com.example.money_up

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import data.MoneyUpDatabase
import data.UserTable.OfflineUsersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        loginBTN.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                //show error if empty field
                Toast.makeText(this, "Fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //Launch a courutine to query db
            CoroutineScope(Dispatchers.IO).launch {
                val user = repository.getUserByUsernameAndPassword(username, password)

                withContext(Dispatchers.Main) {
                    if (user != null) {
                        // Login successful
                        Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT)
                            .show()

                        // Navigate to main/home activity
                        val intent = Intent(this@LoginActivity, HomePageActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Invalid login
                        Toast.makeText(
                            this@LoginActivity,
                            "Invalid username or password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        //handle the functionality of the sign up text
        signuplink.setOnClickListener{
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    }



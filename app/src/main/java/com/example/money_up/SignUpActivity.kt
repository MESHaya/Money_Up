package com.example.money_up

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import data.MoneyUpDatabase
import data.UserTable.OfflineUsersRepository
import data.UserTable.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SignUpActivity : AppCompatActivity() {

    //this repo handles the communication with the database
    private lateinit var repository: OfflineUsersRepository



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        //get instance of the dao from db
        val userDao = MoneyUpDatabase.getDatabase(applicationContext).userDao()
        //pass dao into repo
        repository = OfflineUsersRepository(userDao)

        // Get references to UI elements
        val nameInput = findViewById<EditText>(R.id.name_edit)
        val surnameInput = findViewById<EditText>(R.id.surname_edit)
        val emailInput = findViewById<EditText>(R.id.email_edit)
        val usernameInput = findViewById<EditText>(R.id.username_edit)
        val passwordInput = findViewById<EditText>(R.id.password_edit)
       val confirmPasswordInput= findViewById<EditText>(R.id.confirm_password_edit)
        val signUpButton = findViewById<Button>(R.id.sign_up_button)

        //handle functionality of the sign up button
        signUpButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val surname = surnameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString()

            // Check if all fields are filled
            if (name.isEmpty() || surname.isEmpty() || email.isEmpty()
                || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
            ) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check valid email
            if (!email.contains("@")) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Password and confirm password matching
            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            //create a new user object
                val user = User(
                    name = name,
                    surname = surname,
                    email = email,
                    username = username,
                    password = password

                )


                //insert new user into db via the repo
                CoroutineScope(Dispatchers.IO).launch {

                    repository.insertUser(user)

                    runOnUiThread {
                        Toast.makeText(this@SignUpActivity, "User Registerd!", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }
                }

            }
        }
    }


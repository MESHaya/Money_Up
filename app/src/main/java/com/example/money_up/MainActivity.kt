package com.example.money_up

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_landing_page2)



        val signUpbtn = findViewById<Button>(R.id.Sign_up)
        val loginBtn = findViewById<Button>(R.id.Log_in)

        signUpbtn.setOnClickListener{
            val intent = Intent(this,SignUpActivity:: class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener{
           val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
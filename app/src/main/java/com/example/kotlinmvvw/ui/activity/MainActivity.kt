package com.example.kotlinmvvw.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kotlinmvvw.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Update the ViewCompat call with the new ID
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UI elements with the new IDs
        val emailEditText: EditText = findViewById(R.id.input_email)
        val passwordEditText: EditText = findViewById(R.id.input_password)
        val loginButton: Button = findViewById(R.id.button_sign_in)
        val registerButton: Button = findViewById(R.id.button_sign_up)
        val forgotPasswordText: TextView = findViewById(R.id.text_forgot_password)

        // Example usage of the initialized UI elements
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            // Handle login logic here
        }

        registerButton.setOnClickListener {
            // Handle register logic here
        }

        forgotPasswordText.setOnClickListener {
            // Handle forgot password logic here
        }
    }
}

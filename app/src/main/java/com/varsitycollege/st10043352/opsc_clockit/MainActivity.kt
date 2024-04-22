package com.varsitycollege.st10043352.opsc_clockit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
//iyvbibiubikbYUTCVB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)

        val usernameEditText = findViewById<EditText>(R.id.txtUsername)
        val passwordEditText = findViewById<EditText>(R.id.txtpassword)
        val loginButton = findViewById<Button>(R.id.btnRegister)
        val createAccountTextView = findViewById<TextView>(R.id.create)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (isValidUser(username, password)) {
                // User authenticated successfully
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                // Navigate to the home page
                val intent = Intent(this, home_page::class.java)
                startActivity(intent)
                // Finish the current activity so the user can't navigate back to the login screen using the back button
                finish()
            } else {
                // Invalid username or password
                Toast.makeText(this, "Invalid username or password!", Toast.LENGTH_SHORT).show()
            }
        }

        createAccountTextView.setOnClickListener {
            // Navigate to the register page
            navigateToRegisterPage()
        }
    }

    private fun isValidUser(username: String, password: String): Boolean {
        // Retrieve password for the entered username from SharedPreferences
        val storedPassword = sharedPreferences.getString(username, "")
        // Check if the entered password matches the stored password
        return storedPassword == password
    }

    private fun navigateToRegisterPage() {
        val intent = Intent(this, register_page::class.java)
        startActivity(intent)
       }
}
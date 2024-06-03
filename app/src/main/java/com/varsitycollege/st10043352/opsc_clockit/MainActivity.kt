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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)

        val usernameEditText = findViewById<EditText>(R.id.txtUsername)
        val passwordEditText = findViewById<EditText>(R.id.txtpassword)
        val loginButton = findViewById<Button>(R.id.btnRegister)
        val createAccountTextView = findViewById<TextView>(R.id.create)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                // Show an error message if username or password is empty
                Toast.makeText(this, "Username or password cannot be blank", Toast.LENGTH_SHORT).show()
            } else {
                if (isValidUser(username, password)) {
                    // User authenticated successfully
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    // Navigate to the home page
                    startActivity(Intent(this, home_page::class.java))
                    // Finish the current activity so the user can't navigate back to the login screen using the back button
                    finish()
                } else {
                    // Invalid username or password
                    Toast.makeText(this, "Invalid username or password!", Toast.LENGTH_SHORT).show()
                }
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
        startActivity(Intent(this, register_page::class.java))
    }
}

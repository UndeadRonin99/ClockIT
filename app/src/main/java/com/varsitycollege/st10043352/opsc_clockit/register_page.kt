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

class register_page : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)

        sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)

        val usernameEditText = findViewById<EditText>(R.id.txtRUsername)
        val passwordEditText = findViewById<EditText>(R.id.txtRpassword)
        val confirmPasswordEditText = findViewById<EditText>(R.id.txtCPassword)
        val registerButton = findViewById<Button>(R.id.btnRegister)
        val loginTextView = findViewById<TextView>(R.id.textView2)

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (password == confirmPassword) {
                if (isUsernameAvailable(username)) {
                    // Save the new user to SharedPreferences
                    saveUser(username, password)

                    usernameEditText.text.clear()
                    passwordEditText.text.clear()
                    confirmPasswordEditText.text.clear()
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@register_page, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Username already exists!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
            }
        }

        // Set click listener for "Login" text view
        loginTextView.setOnClickListener {
            val intent = Intent(this@register_page, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isUsernameAvailable(username: String): Boolean {
        // Check if the username is already in use
        return !sharedPreferences.contains(username)
    }

    private fun saveUser(username: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString(username, password)
        editor.apply()
       }
}
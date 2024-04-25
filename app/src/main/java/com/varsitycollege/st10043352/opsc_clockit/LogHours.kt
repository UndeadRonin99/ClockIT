package com.varsitycollege.st10043352.opsc_clockit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LogHours : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_hours)

        val activityData = intent.getStringExtra("activityData") ?: ""
        val activityTextView = findViewById<TextView>(R.id.textView_activity_name)
        val categoryTextView = findViewById<TextView>(R.id.textView_activity_category)

        // Split the activity details
        val details = activityData.split(",")
        if (details.size >= 3) { // Ensure we have at least three elements (name, description, category)
            val activityName = details[0] // Activity Name
            val categoryName = details[2] // Category Name

            activityTextView.text = activityName
            categoryTextView.text = categoryName
        }

        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            // Navigate back to the home page
            val intent = Intent(this, home_page::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Clear the back stack
            startActivity(intent)
            finish() // Finish current activity
        }
    }
}
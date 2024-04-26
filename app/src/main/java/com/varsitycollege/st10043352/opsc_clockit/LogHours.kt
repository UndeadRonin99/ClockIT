package com.varsitycollege.st10043352.opsc_clockit

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LogHours : AppCompatActivity() {

    private val SHARED_PREF_KEY = "goals"
    private val MIN_GOAL_KEY = "min_goal"
    private val MAX_GOAL_KEY = "max_goal"

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_hours)

        sharedPreferences = getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE)

        val activityData = intent.getStringExtra("activityData") ?: ""
        val activityTextView = findViewById<TextView>(R.id.textView_activity_name)
        val categoryTextView = findViewById<TextView>(R.id.textView_activity_category)
        val dailyGoalsTextView = findViewById<TextView>(R.id.textView11) // TextView for daily goals

        // Split the activity details
        val details = activityData.split(",")
        if (details.size >= 3) { // Ensure we have at least three elements (name, description, category)
            val activityName = details[0] // Activity Name
            val categoryName = details[2] // Category Name

            activityTextView.text = activityName
            categoryTextView.text = categoryName
            categoryTextView.setTextColor((details.get(3)).toInt())

            // Retrieve and display saved daily goals
            val minGoal = sharedPreferences.getString("$MIN_GOAL_KEY-$activityName", "")
            val maxGoal = sharedPreferences.getString("$MAX_GOAL_KEY-$activityName", "")

            val dailyGoalsText = StringBuilder()
            if (!minGoal.isNullOrEmpty()) {
                dailyGoalsText.append("Min Goal: $minGoal\n")
            }

            if (!maxGoal.isNullOrEmpty()) {
                dailyGoalsText.append("\nMax Goal: $maxGoal")
            }

            dailyGoalsTextView.text = dailyGoalsText.toString()
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
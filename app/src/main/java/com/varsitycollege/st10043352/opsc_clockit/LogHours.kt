package com.varsitycollege.st10043352.opsc_clockit

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LogHours : AppCompatActivity() {

    private val SHARED_PREF_KEY = "goals"
    private val MIN_GOAL_KEY = "min_goal"
    private val MAX_GOAL_KEY = "max_goal"

    private lateinit var sharedPreferences: SharedPreferences
    private var details: List<String> = listOf("")
    private var activityData: String = ""
    private lateinit var sharedActivities: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_hours)

        sharedPreferences = getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE)

        activityData = intent.getStringExtra("activityData") ?: ""
        val activityTextView = findViewById<TextView>(R.id.txtActivity1)
        val categoryTextView = findViewById<TextView>(R.id.txtCategory1)
        val dailyGoalsTextView = findViewById<TextView>(R.id.textView11) // TextView for daily goals
        val button = findViewById<Button>(R.id.btnLogHours)

        // Split the activity details
        details = activityData.split(",")
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

            button.setOnClickListener{
                val intent = Intent(this, SessionLog::class.java)
                intent.putExtra("activity", activityData)
                startActivity(intent)
            }


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

    fun formatActivities(log: String?): List<String> {
        var logData: List<String> = emptyList()

        log?.let {
            logData = log.split(",")
        }

        return logData
    }

    fun DeleteOnClick(view: View){
        sharedActivities = getSharedPreferences("CategoryPreferences", MODE_PRIVATE)
        val allActivities = sharedActivities.all
        for ((key1, value) in allActivities) {
            if (key1.startsWith("activity_")) { // Check if the key represents an activity
                val activityList = formatActivities(value.toString())
                if(details[0].equals(activityList[0])){
                    val editor = sharedActivities.edit()
                    editor.remove(key1)
                    editor.apply()
                    Log.d("Delete", "activity deleted")
                    Toast.makeText(this, "Activity Deleted", Toast.LENGTH_SHORT).show()
                    finish()
                    break
                }
            }
        }
    }

}
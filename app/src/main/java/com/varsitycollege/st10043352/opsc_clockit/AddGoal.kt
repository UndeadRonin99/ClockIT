package com.varsitycollege.st10043352.opsc_clockit

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity

class AddGoal : AppCompatActivity() {

    private val SHARED_PREF_KEY = "goals"
    private val MIN_GOAL_KEY = "min_goal"
    private val MAX_GOAL_KEY = "max_goal"

    private lateinit var txtActivity: TextView
    private lateinit var txtCategory: TextView
    private lateinit var btnMin: Button
    private lateinit var btnMax: Button
    private lateinit var txtMin: TextView
    private lateinit var txtMax: TextView
    private lateinit var minPicker: TimePicker
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_goal)

        // Initialize all fields
        txtActivity = findViewById(R.id.txtActivity1)
        txtCategory = findViewById(R.id.txtCategory1)
        btnMin = findViewById(R.id.btnAddMin)
        btnMax = findViewById(R.id.btnAddMax)
        txtMin = findViewById(R.id.txtMin)
        txtMax = findViewById(R.id.txtMax)
        minPicker = findViewById(R.id.spnrTime)

        minPicker.setIs24HourView(true)

        // Retrieve the information from Intent extras
        val activityData = intent.getStringExtra("activityData")

        // Split the activityData string into an array of relevant fields
        val activityFields = splitActivityData(activityData)

        txtActivity.text = activityFields?.get(0)
        txtCategory.text = activityFields?.get(2)
        txtCategory.setTextColor((activityFields?.get(3))?.toInt() ?: 0)

        sharedPreferences = getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE)

        btnMin.setOnClickListener {
            val selectedHour = minPicker.hour
            val selectedMinute = minPicker.minute
            val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            txtMin.text = "Min goal: $selectedTime"

            // Pass the activity name when saving min goal
            saveMinGoal(txtActivity.text.toString(), selectedTime)
        }

        btnMax.setOnClickListener {
            val selectedHour = minPicker.hour
            val selectedMinute = minPicker.minute
            val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            txtMax.text = "Max goal: $selectedTime"

            // Pass the activity name when saving max goal
            saveMaxGoal(txtActivity.text.toString(), selectedTime)
        }

    }

    private fun splitActivityData(activityData: String?): List<String>? {
        return activityData?.split(",")?.map { it.trim() }
    }

    fun back(view: View) {
        finish()
    }

    private fun saveMinGoal(activity: String, minGoal: String) {
        val minGoalKey = "$MIN_GOAL_KEY-$activity"
        sharedPreferences.edit().putString(minGoalKey, minGoal).apply()
    }

    private fun saveMaxGoal(activity: String, maxGoal: String) {
        val maxGoalKey = "$MAX_GOAL_KEY-$activity"
        sharedPreferences.edit().putString(maxGoalKey, maxGoal).apply()
    }

}

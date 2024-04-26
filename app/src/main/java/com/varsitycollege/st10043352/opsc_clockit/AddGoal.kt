package com.varsitycollege.st10043352.opsc_clockit

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity

class AddGoal : AppCompatActivity() {

    private lateinit var txtActivity: TextView
    private lateinit var txtCategory: TextView
    private lateinit var btnMin: Button
    private lateinit var txtMin: TextView
    private lateinit var timePicker: TimePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_goal)

        // Initialize all fields
        txtActivity = findViewById(R.id.txtActivity)
        txtCategory = findViewById(R.id.txtCategory)
        btnMin = findViewById(R.id.btnAddMin)
        txtMin = findViewById(R.id.txtMin)
        timePicker = findViewById(R.id.timePicker1)

        // Retrieve the information from Intent extras
        val activityData = intent.getStringExtra("activityData")

        // Split the activityData string into an array of relevant fields
        val activityFields = splitActivityData(activityData)

        txtActivity.text = activityFields?.get(0)
        txtCategory.text = activityFields?.get(2)
        txtCategory.setTextColor((activityFields?.get(3))?.toInt() ?: 0)

        btnMin.setOnClickListener {
            val selectedHour = timePicker.hour
            val selectedMinute = timePicker.minute
            val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            txtMin.text = "Min goal: $selectedTime"
        }
    }

    private fun splitActivityData(activityData: String?): List<String>? {
        return activityData?.split(",")?.map { it.trim() }
    }

    fun back(view: View) {
        finish()
    }
}

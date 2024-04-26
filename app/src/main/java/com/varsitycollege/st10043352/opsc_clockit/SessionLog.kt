package com.varsitycollege.st10043352.opsc_clockit

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity

private lateinit var sharedPreferences: SharedPreferences
private lateinit var spnrTime: TimePicker
private lateinit var btnLog: Button
private lateinit var btnAddPhoto: Button
private lateinit var btnSave: Button
private lateinit var txtActivity: TextView
private lateinit var txtCategory: TextView



class SessionLog : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session_log)

        sharedPreferences = getSharedPreferences("categoryPreferences", MODE_PRIVATE)
        val activityData = intent.getStringExtra("activity") ?: ""

        txtActivity = findViewById(R.id.txtActivity1)
        txtCategory = findViewById(R.id.txtCategory1)
        spnrTime = findViewById(R.id.spnrTime)
        spnrTime.setIs24HourView(true)

        val details = activityData.split(",")
        if (details.size >= 3) { // Ensure we have at least three elements (name, description, category)
            val activityName = details[0] // Activity Name
            val categoryName = details[2] // Category Name

            txtActivity.text = activityName
            txtCategory.text = categoryName
            txtCategory.setTextColor((details.get(3)).toInt())
        }

        }
}
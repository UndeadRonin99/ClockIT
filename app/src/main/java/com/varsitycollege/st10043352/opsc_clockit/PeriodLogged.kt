package com.varsitycollege.st10043352.opsc_clockit

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PeriodLogged : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_period_logged)

        // Retrieve the activity data passed via intent
        val activityData = intent.getStringExtra("activityData") ?: ""

        // Find the TextView in the layout
        val txtActivity1 = findViewById<TextView>(R.id.txtActivity1)

        // Set the text of the TextView with the activity name
        txtActivity1.text = activityData.split(",")[0] // Assuming activity name is the first element

        val backButton = findViewById <ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            // Navigate back to the statisticsPeriod activity
            onBackPressed()
        }


    }

}

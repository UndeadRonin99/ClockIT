package com.varsitycollege.st10043352.opsc_clockit

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddGoal : AppCompatActivity() {

    private lateinit var txtActivity: TextView
    private lateinit var txtCategory: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_goal)

        //intialise all fields
        txtActivity = findViewById(R.id.txtActivity)
        txtCategory = findViewById(R.id.txtCategory)

        // Retrieve the information from Intent extras
        val activityData = intent.getStringExtra("activityData")

        // Split the activityData string into an array of relevant fields
        val activityFields = splitActivityData(activityData)

        txtActivity.setText(activityFields?.get(0))
        txtCategory.setText(activityFields?.get(2))
        txtCategory.setTextColor((activityFields?.get(3))?.toInt() ?: 0)
        /*
        name = activityFields?.get(0) ?: ""
        description = activityFields?.get(1) ?: ""
        category = activityFields?.get(2) ?: ""
        color = activityFields?.get(3) ?: ""
        number1 = activityFields?.get(4) ?: ""
        number2 = activityFields?.get(5) ?: ""
        contentUri = activityFields?.get(6) ?: ""
        */

        // Now you can use these variables as needed in your activity
    }

    private fun splitActivityData(activityData: String?): List<String>? {
        return activityData?.split(",")?.map { it.trim() }
    }
    fun back(view: View){
        finish()
    }
}
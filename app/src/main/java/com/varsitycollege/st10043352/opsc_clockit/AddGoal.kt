package com.varsitycollege.st10043352.opsc_clockit

import android.content.Intent
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
        // Now you have an array of relevant fields that you can use as needed
        /* For example:
        val name = activityFields?.get(0) ?: ""
        val description = activityFields?.get(1) ?: ""
        val category = activityFields?.get(2) ?: ""
        val color = activityFields?.get(3) ?: ""
        val number1 = activityFields?.get(4) ?: ""
        val number2 = activityFields?.get(5) ?: ""
        val contentUri = activityFields?.get(6) ?: ""
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
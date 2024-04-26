package com.varsitycollege.st10043352.opsc_clockit

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AddGoal : AppCompatActivity() {

    private lateinit var txtActivity: TextView
    private lateinit var txtCategory: TextView
    private lateinit var btnMin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_goal)

        //intialise all fields
        txtActivity = findViewById(R.id.txtActivity)
        txtCategory = findViewById(R.id.txtCategory)
        btnMin = findViewById(R.id.btnAddMin)

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

        // Find the Spinner view by its ID
        val spinnerMin1: Spinner = findViewById(R.id.spnrMinHrs)
        val spinnerMin2: Spinner = findViewById(R.id.spnrMinMinutes)



        //Create an ArrayAdapter using the string array and a default spinner layout
        val adapterHours = ArrayAdapter.createFromResource(this, R.array.hours_array,android.R.layout.simple_spinner_dropdown_item)


        val adapterMinutes = ArrayAdapter.createFromResource(
            this,
            R.array.minutes_array,
            android.R.layout.simple_spinner_item
        )


        // Specify the layout to use when the list of choices appears
        adapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapterMinutes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        spinnerMin1.adapter = adapterHours
        spinnerMin2.adapter = adapterMinutes

        // Set text color and background color for the spinners
        spinnerMin1.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Set text color for selected item
                (view as TextView).setTextColor(android.graphics.Color.WHITE)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        })

        spinnerMin2.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Set text color for selected item
                (view as TextView).setTextColor(android.graphics.Color.WHITE)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        })

// Set background color for the spinners

        spinnerMin1.setBackgroundColor(android.graphics.Color.parseColor("#1B232E"))
        spinnerMin1.setBackgroundColor(android.graphics.Color.parseColor("#1B232E"))

        btnMin.setOnClickListener {

        }
    }



    private fun splitActivityData(activityData: String?): List<String>? {
        return activityData?.split(",")?.map { it.trim() }
    }
    fun back(view: View){
        finish()
    }
}
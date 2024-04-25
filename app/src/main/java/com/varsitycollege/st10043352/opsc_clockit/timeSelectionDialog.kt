package com.varsitycollege.st10043352.opsc_clockit

import CustomSpinnerAdapter
import android.os.Bundle
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class timeSelectionDialog : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.time_selection_dialog)

        val spinnerHours: Spinner = findViewById(R.id.spinnerHours)
        val spinnerMinutes: Spinner = findViewById(R.id.spinnerMinutes)

        // Assuming you have arrays for hours and minutes
        val hoursArray : Array<String> // Options for hours
        val minutesArray : Array<String>



        // Creating custom adapters
        val hoursAdapter = CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, hoursArray)
        val minutesAdapter = CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, minutesArray)

        // Setting adapters to Spinners
        spinnerHours.adapter = hoursAdapter
        spinnerMinutes.adapter = minutesAdapter
    }
}
// AddActivity.kt

package com.varsitycollege.st10043352.opsc_clockit

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray

// Activity for adding a new activity
class AddActivity : AppCompatActivity() {

    private lateinit var colorBox: View
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var spinner: Spinner
    private lateinit var doneButton: Button
    private lateinit var categoryColors: MutableList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("CategoryPreferences", MODE_PRIVATE)
        // Initialize elements
        colorBox = findViewById(R.id.colorBox)
        spinner = findViewById(R.id.spinner)
        doneButton = findViewById(R.id.button2)
        // Retrieve data from SharedPreferences
        val categoriesJsonString = sharedPreferences.getString("categories", null)
        // Populate spinner with retrieved data
        populateSpinner(categoriesJsonString)

        // Set onClickListener for the done button to finish activity
        doneButton.setOnClickListener{
            finish()
        }

        // Set up the spinner listener
        setupSpinnerListener()
    }

    // Function to populate spinner with category names
    private fun populateSpinner(categoriesJsonString: String?) {
        categoriesJsonString?.let {
            // Convert JSON string to JSONArray
            val jsonArray = JSONArray(it)

            // Extract category names and colors from JSONArray
            val categoryNames = mutableListOf<String>()
            categoryColors = mutableListOf()
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val categoryName = jsonObject.getString("categoryName")
                val categoryColor = jsonObject.getInt("categoryColor")
                categoryNames.add(categoryName)
                categoryColors.add(categoryColor)
            }

            // Create adapter for spinner
            val adapter = object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categoryNames) {
                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getDropDownView(position, convertView, parent)
                    if (view is TextView) {
                        view.setTextColor(Color.WHITE) // Set text color for dropdown items
                        view.setBackgroundColor(Color.parseColor("#1B232E")) // Set background color for dropdown items
                    }
                    return view
                }

                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    if (view is TextView) {
                        view.setTextColor(Color.WHITE) // Set text color for selected item
                    }
                    return view
                }
            }
            spinner.adapter = adapter
        }
    }

    // Function to set up the spinner listener
    private fun setupSpinnerListener() {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Update the color of the colorBox
                Log.d("update","color should update")
                if (position != categoryColors.size) {
                    colorBox.setBackgroundColor(categoryColors[position])
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }
}

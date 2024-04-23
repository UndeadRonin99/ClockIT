package com.varsitycollege.st10043352.opsc_clockit

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject

// Activity for adding a new category
class Add_Category : AppCompatActivity() {

    private lateinit var categoryNameEditText: EditText
    private lateinit var doneButton: Button
    private lateinit var colorViews: List<ImageView>
    private var selectedColor: Int = Color.RED // Default to Color.WHITE or any other default color

    // SharedPreferences for storing category name and color
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        // Initialize views
        categoryNameEditText = findViewById(R.id.txtCategoryName)
        doneButton = findViewById(R.id.button2)
        colorViews = listOf(
            findViewById(R.id.colorOption1),
            findViewById(R.id.colorOption2),
            findViewById(R.id.colorOption3),
            findViewById(R.id.colorOption4),
            findViewById(R.id.colorOption5)
        )

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("CategoryPreferences", Context.MODE_PRIVATE)

        // Set onClickListener for color options
        for (i in 0 until colorViews.size) {
            colorViews[i].setOnClickListener {
                // Set the selected colour as the background colour of the EditText
                categoryNameEditText.setBackgroundColor(getColorOption(i))
                // Get the selected color
                selectedColor = getColorOption(i)
            }
        }
        // Set onClickListener for the done button
        doneButton.setOnClickListener {

            // Get the category name entered by the user
            val categoryName = categoryNameEditText.text.toString().trim()



            // Save category name and color to SharedPreferences
            saveCategory(categoryName, selectedColor)

            // Navigate back to the main page
            finish()
        }


    }

    // Function to get the selected color

    // Function to get color option based on index
    private fun getColorOption(index: Int): Int {
        // Define your colour options here
        val colorOptions = listOf(
            Color.RED,
            Color.BLUE,
            Color.GREEN,
            Color.YELLOW,
            Color.MAGENTA
        )
        return colorOptions[index]
    }

    // Function to save category to SharedPreferences
    private fun saveCategory(categoryName: String, color: Int) {
        // Retrieve existing categories
        val categoriesJsonString = sharedPreferences.getString("categories", null)
        val categoriesArray = if (categoriesJsonString != null) {
            JSONArray(categoriesJsonString)
        } else {
            JSONArray()
        }

        // Create JSON object for the new category
        val categoryObject = JSONObject().apply {
            put("categoryName", categoryName)
            put("categoryColor", color)
        }

        // Append new category to the existing list
        categoriesArray.put(categoryObject)

        // Save updated categories to SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString("categories", categoriesArray.toString())
        editor.apply()
    }
}

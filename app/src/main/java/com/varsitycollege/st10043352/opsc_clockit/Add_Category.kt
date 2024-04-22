package com.varsitycollege.st10043352.opsc_clockit

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Add_Category : AppCompatActivity() {

    private lateinit var categoryNameEditText: EditText
    private lateinit var doneButton: Button
    private lateinit var colorViews: List<ImageView>

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

        // Set onClickListener for the done button
        doneButton.setOnClickListener {

            // Get the category name entered by the user
            val categoryName = categoryNameEditText.text.toString().trim()

            // Get the selected color
            val selectedColor = getSelectedColor()

            // Save category name and color to SharedPreferences
            saveCategory(categoryName, selectedColor)

            // Navigate back to the main page
            finish()
        }

        // Set onClickListener for color options
        for (i in 0 until colorViews.size) {
            colorViews[i].setOnClickListener {

                // Set the selected colour as the background colour of the EditText
                categoryNameEditText.setBackgroundColor(getColorOption(i))
            }
        }
    }

    private fun getSelectedColor(): Int {
        // Find the selected colour view
        for (i in 0 until colorViews.size) {
            if (colorViews[i].isSelected) {
                return getColorOption(i)
            }
        }
        // Default colour if no colour is selected
        return Color.WHITE
    }

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

    private fun saveCategory(categoryName: String, color: Int) {
        // Save category name and colour to SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString("categoryName", categoryName)
        editor.putInt("categoryColor", color)
        editor.apply()
    }
}

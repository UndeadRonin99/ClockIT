// AddActivity.kt

package com.varsitycollege.st10043352.opsc_clockit


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import android.widget.ImageView

class AddActivity : AppCompatActivity() {

    private lateinit var colorBox: View
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var spinner: Spinner
    private lateinit var doneButton: Button
    private lateinit var categoryColors: MutableList<Int>

    // Constants for image selection
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("CategoryPreferences", MODE_PRIVATE)

        // Initialize elements
        colorBox = findViewById(R.id.colorBox)
        spinner = findViewById(R.id.spinner)
        doneButton = findViewById(R.id.btnAddActivity)

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

        // Set up click listener for the add photo button
        val btnAddPhoto = findViewById<Button>(R.id.btnAddPhoto)
        btnAddPhoto.setOnClickListener {
            openImagePicker()
        }
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

    // Function to open image picker
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // Function to handle result from image picker
    // Function to handle result from image picker
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Get selected image URI
            val imageUri: Uri? = data.data
            // Display preview of selected image
            if (imageUri != null) {
                findViewById<ImageView>(R.id.imgPreview).apply {
                    setImageURI(imageUri)
                    visibility = View.VISIBLE // Show the preview ImageView
                }
            }
            // Save the image URI to SharedPreferences or process it as needed
            saveImageUri(imageUri)
        }
    }

    // Function to save the image URI to SharedPreferences
    private fun saveImageUri(imageUri: Uri?) {
        // Save the image URI to SharedPreferences here
        // You can use SharedPreferences to store the image URI as a string
    }
}


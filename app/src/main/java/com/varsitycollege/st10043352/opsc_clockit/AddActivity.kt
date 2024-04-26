package com.varsitycollege.st10043352.opsc_clockit

import android.app.TimePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import java.util.Calendar
import java.util.Locale

class AddActivity : AppCompatActivity() {

    private lateinit var colorBox: View
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedActivities: SharedPreferences
    private lateinit var spinner: Spinner
    private lateinit var doneButton: Button
    private lateinit var categoryColors: MutableList<Int>
    private lateinit var txtActivityName: EditText
    private lateinit var txtDescription: EditText
    private lateinit var txtStartTime: EditText
    private lateinit var txtEndTime: EditText
    private lateinit var imgPreview: ImageView
    private var photoUri: Uri? = null

    // Constants for image selection
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("CategoryPreferences", MODE_PRIVATE)
        sharedActivities = getSharedPreferences("CategoryPreferences", MODE_PRIVATE)

        // Initialize elements
        colorBox = findViewById(R.id.colorBox)
        spinner = findViewById(R.id.spnrMinMinutes)
        doneButton = findViewById(R.id.btnAddMin)
        txtActivityName = findViewById(R.id.txtActivityName)
        txtDescription = findViewById(R.id.txtDescription)
        txtStartTime = findViewById(R.id.txtStartTime)
        txtEndTime = findViewById(R.id.txtEndTime)
        imgPreview = findViewById(R.id.imgPreview)

        // Retrieve data from SharedPreferences
        val categoriesJsonString = sharedPreferences.getString("categories", null)

        // Populate spinner with retrieved data
        populateSpinner(categoriesJsonString)

        // Set onClickListener for the done button to finish activity
        doneButton.setOnClickListener {
            saveActivity()
            finish()
        }

        // Set up the spinner listener
        setupSpinnerListener()

        // Set up click listener for the add photo button
        val btnAddPhoto = findViewById<Button>(R.id.btnAddPhoto)
        btnAddPhoto.setOnClickListener {
            openImagePicker()
        }

        // Set up click listener for the start time EditText
        txtStartTime.setOnClickListener {
            showTimePickerDialog(txtStartTime)
        }

        // Set up click listener for the end time EditText
        txtEndTime.setOnClickListener {
            showTimePickerDialog(txtEndTime)
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
                        view.setBackgroundColor(Color.TRANSPARENT) // Set background color for selected item
                    }
                    return view
                }
            }

            // Set adapter for spinner
            spinner.adapter = adapter
        }
    }

    // Function to set up listener for spinner
    private fun setupSpinnerListener() {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Set color for color box based on selected category
                colorBox.setBackgroundColor(categoryColors[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    // Function to open image picker
    private fun openImagePicker() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    // Function to handle result of image picker
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            photoUri = data.data
            imgPreview.setImageURI(photoUri)
        }
    }

    // Function to save activity details
    private fun saveActivity() {
        val activityName = txtActivityName.text.toString()
        val description = txtDescription.text.toString()
        val categoryName = spinner.selectedItem.toString()
        val color = categoryColors[spinner.selectedItemPosition]
        val startTime = txtStartTime.text.toString()
        val endTime = txtEndTime.text.toString()

        if (activityName == null || activityName.equals("")) {

        } else {
            // Create CSV string
            val csvString =
                "$activityName,$description,$categoryName,$color,$startTime,$endTime,${photoUri?.toString() ?: ""}"

            // Save CSV string to SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString("activity_${System.currentTimeMillis()}", csvString)
            editor.apply()
        }
    }

    // Function to show time picker dialog
    private fun showTimePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                // Update EditText with selected time
                val selectedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
                editText.setText(selectedTime)
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }

}
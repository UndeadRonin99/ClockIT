package com.varsitycollege.st10043352.opsc_clockit

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri

class SessionLog : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var spnrTime: TimePicker
    private lateinit var btnLog: Button
    private lateinit var btnAddPhoto: Button
    private lateinit var btnSave: Button
    private lateinit var txtActivity: TextView
    private lateinit var txtCategory: TextView
    private lateinit var imgPreview: ImageView
    private lateinit var datePicker: DatePicker
    private var photoUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session_log)

        sharedPreferences = getSharedPreferences("CategoryPreferences", MODE_PRIVATE)
        val activityData = intent.getStringExtra("activity") ?: ""

        txtActivity = findViewById(R.id.txtActivity1)
        txtCategory = findViewById(R.id.txtCategory1)
        btnAddPhoto = findViewById(R.id.btnAddPhoto)
        btnSave = findViewById(R.id.btnSave)
        datePicker = findViewById(R.id.DatePicker)
        imgPreview = findViewById(R.id.imgPreview) // Initialize imgPreview here

        spnrTime = findViewById(R.id.spnrTime)
        spnrTime.setIs24HourView(true)

        val details = activityData.split(",")
        if (details.size >= 3) { // Ensure we have at least three elements (name, description, category)
            val activityName = details[0] // Activity Name
            val categoryName = details[2] // Category Name

            txtActivity.text = activityName
            txtCategory.text = categoryName
            txtCategory.setTextColor((details.get(3)).toInt())
        }

        btnAddPhoto.setOnClickListener {
            openImagePicker1()
        }

        btnSave.setOnClickListener{
            val selectedHour = spnrTime.hour
            val selectedMinute = spnrTime.minute
            val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)

            val selectedMonth = datePicker.month + 1
            val day = datePicker.dayOfMonth
            val selectedYear = datePicker.year
            val selectedDate = String.format("%02d/%02d", day, selectedMonth, selectedYear)


            // Concatenate all the details into a single string
            val logEntry = "${details[0]},${details[2]},${details[3]},$selectedTime,$selectedDate,${photoUri.toString()}"

            // Save the updated log data back to SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString("Log_${System.currentTimeMillis()}", logEntry)
            editor.apply()

            Toast.makeText(this, "Session logged", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    private fun openImagePicker1() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    // Function to handle result of image picker
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imgPreview = findViewById(R.id.imgPreview)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            photoUri = data.data
            val dataUri = (data.data).toString()
            val dataIntent = dataUri.toUri()
            imgPreview.setImageURI(photoUri)
        }
    }

    fun back1(view: View){
        finish()
    }
}
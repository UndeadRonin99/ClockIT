package com.varsitycollege.st10043352.opsc_clockit

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream

class SessionLog : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private val REQUEST_IMAGE_CAPTURE = 2

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
        imgPreview = findViewById(R.id.imgPreview)

        spnrTime = findViewById(R.id.spnrTime)
        spnrTime.setIs24HourView(true)

        val details = activityData.split(",")
        if (details.size >= 3) {
            val activityName = details[0]
            val categoryName = details[2]

            txtActivity.text = activityName
            txtCategory.text = categoryName
            txtCategory.setTextColor((details.get(3)).toInt())
        }

        btnAddPhoto.setOnClickListener {
            openImagePicker()
        }

        btnSave.setOnClickListener{
            val selectedHour = spnrTime.hour
            val selectedMinute = spnrTime.minute
            val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)

            val selectedMonth = datePicker.month + 1
            val day = datePicker.dayOfMonth
            val selectedYear = datePicker.year
            val selectedDate = String.format("%02d/%02d", day, selectedMonth, selectedYear)

            val logEntry = "${details[0]},${details[2]},${details[3]},$selectedTime,$selectedDate,${photoUri.toString()}"

            val editor = sharedPreferences.edit()
            editor.putString("Log_${System.currentTimeMillis()}", logEntry)
            editor.apply()

            Toast.makeText(this, "Session logged", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun openImagePicker() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            photoUri = data.data
            imgPreview.setImageURI(photoUri)
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val uri = saveImageToExternalStorage(imageBitmap)
            imgPreview.setImageBitmap(imageBitmap)
            photoUri = uri
        }
    }

    private fun saveImageToExternalStorage(bitmap: Bitmap): Uri {
        val imagesFolder = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "SessionImages")
        if (!imagesFolder.exists()) {
            imagesFolder.mkdirs()
        }
        val imageFile = File(imagesFolder, "session_image_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.close()
        return imageFile.toUri()
    }

    fun takePhoto(view: View) {
        dispatchTakePictureIntent()
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    fun back1(view: View){
        finish()
    }
}

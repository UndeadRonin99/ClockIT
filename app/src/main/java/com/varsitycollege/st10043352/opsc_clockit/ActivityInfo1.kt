package com.varsitycollege.st10043352.opsc_clockit

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Picasso

class ActivityInfo1 : AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info1)

        // Retrieve data from the intent extras
        val activityName = intent.getStringExtra("activityName")
        val description = intent.getStringExtra("description")
        val category = intent.getStringExtra("category")
        val startTime = intent.getStringExtra("startTime")
        val endTime = intent.getStringExtra("endTime")
        val photoUri = intent.getStringExtra("photoUri")

        // Find Views in the layout
        val txtHeading = findViewById<TextView>(R.id.txtHeading)
        val txtActivityName = findViewById<TextView>(R.id.ActivityName)
        val txtDescription = findViewById<TextView>(R.id.txtDescription)
        val txtCategory = findViewById<TextView>(R.id.txtCategory)
        val txtStartTime = findViewById<TextView>(R.id.txtStartTime)
        val txtEndTime = findViewById<TextView>(R.id.txtEndTime)
        val txtPhotoUri = findViewById<TextView>(R.id.txtPhotoUri)
        backButton = findViewById(R.id.back_button)
        imageView = findViewById(R.id.imageView)

        // Load image into the ImageView using Picasso
        Picasso.get().load(photoUri).into(imageView)

        backButton.setOnClickListener {
            finish() // Finish current activity
        }

        // Set the retrieved data to the TextViews
        txtHeading.text = activityName
        txtActivityName.text = activityName
        txtDescription.text = description
        txtCategory.text = category
        txtStartTime.text = startTime
        txtEndTime.text = endTime
        txtPhotoUri.text = photoUri
    }
}
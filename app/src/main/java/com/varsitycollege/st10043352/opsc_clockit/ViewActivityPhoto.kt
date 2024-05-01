package com.varsitycollege.st10043352.opsc_clockit

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ViewActivityPhoto : AppCompatActivity() {

    private lateinit var txtName: TextView
    private lateinit var photo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_photo)

        txtName = findViewById(R.id.txtName)
        photo = findViewById(R.id.photo1)

        val imageUri = intent.getStringExtra("uri")
        val activityName = intent.getStringExtra("activityName")

        txtName.setText(activityName)

        val image = imageUri?.toUri()

        photo.setImageURI(image)
    }
}
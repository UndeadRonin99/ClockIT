package com.varsitycollege.st10043352.opsc_clockit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class ViewLog : AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var txtName: TextView
    private lateinit var txtCategory: TextView
    private lateinit var txtTime: TextView
    private lateinit var LogPhoto: ImageView
    private var logList: List<String> = listOf("")
    private lateinit var txt404: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_log)

        backButton = findViewById(R.id.back_button)
        txtName = findViewById(R.id.ActivityName)
        txtCategory = findViewById(R.id.CategoryName)
        txtTime = findViewById(R.id.TimeOnTask)
        LogPhoto = findViewById(R.id.LogPhoto)
        txt404 = findViewById(R.id.txt404)

        val activityData = intent.getStringExtra("activityData")
        val logData = intent.getStringExtra("logData")

        val activityList = formatActivities(activityData)
        logList = formatLogs(logData)
        val logTime = formatSharedPref(logData)

        txtName.text = activityList.getOrNull(0) ?: "Activity Name Not Found"
        txtCategory.text = activityList.getOrNull(2) ?: "Category Not Found"
        txtTime.text = logTime

        if(!logList[5].equals("")){
            loadLogImage(logList)
        } else {
            txt404.isVisible = true
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun loadLogImage(logList: List<String>) {
        if (logList.size >= 6 && !logList[5].isNullOrEmpty()) {
            val logImageUri = Uri.parse(logList[5])
            if (logImageUri.scheme != null && logImageUri.host != null) {
                // Load image from Firebase Storage using Picasso
                FirebaseStorage.getInstance().getReferenceFromUrl(logList[5]).downloadUrl
                    .addOnSuccessListener { uri ->
                        Picasso.get().load(uri).into(LogPhoto)
                    }
                    .addOnFailureListener { exception ->
                        Log.e("ViewLog", "Failed to load image: $exception")
                    }
            } else {
                Log.e("ViewLog", "Invalid URI: $logImageUri")
            }
        } else {
            Log.e("ViewLog", "URI string is null or invalid.")
        }
    }


    companion object {
        fun formatLogs(log: String?): List<String> {
            return log?.split(",") ?: emptyList()
        }

        fun formatActivities(log: String?): List<String> {
            return log?.split(",") ?: emptyList()
        }

        fun formatSharedPref(activity: String?): CharSequence? {
            return activity?.let {
                val values = activity.split(",")
                val name = values[3]
                val times = name.split(":")
                "\t${times[0]} hours ${times[1]} minutes"
            } ?: ""
        }
    }
}

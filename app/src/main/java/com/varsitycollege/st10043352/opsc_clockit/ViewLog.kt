package com.varsitycollege.st10043352.opsc_clockit

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ViewLog : AppCompatActivity() {
    private lateinit var backButton: ImageView
    private lateinit var txtName: TextView
    private lateinit var txtCategory: TextView
    private lateinit var txtTime: TextView
    private lateinit var LogPhoto: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_log)

        //declare elements on screen
        backButton = findViewById(R.id.back_button)
        txtName = findViewById(R.id.ActivityName)
        txtCategory = findViewById(R.id.CategoryName)
        txtTime = findViewById(R.id.TimeOnTask)
        LogPhoto = findViewById(R.id.LogPhoto)

        val activityData = intent.getStringExtra("activityData")
        val logData = intent.getStringExtra("logData")

        val activityList = formatActivities(activityData)
        val logList = formatLogs(logData)
        val logTime = formatSharedPref(logData)

        txtName.setText(activityList[0])
        txtCategory.setText(activityList[2])

        txtTime.setText(logTime)

        val logImageURI = Uri.parse(logList[5])

        LogPhoto.setImageURI(logImageURI)



        backButton.setOnClickListener{
            finish()
        }

    }

    fun formatLogs(log: String?): List<String> {
        var logData: List<String> = emptyList()

        log?.let {
            logData = log.split(",")
        }

        return logData
    }

    fun formatActivities(log: String?): List<String> {
        var logData: List<String> = emptyList()

        log?.let {
            logData = log.split(",")
        }

        return logData
    }

    fun formatSharedPref(activity: String?): CharSequence? {
        var activityDetails = ""
        activity?.let {
            val values = activity.split(",")

            val name = values[3]

            val times = name.split(":")

            activityDetails= "\t${times[0]} hours ${times[1]} minutes"
        }
        return activityDetails
    }
}
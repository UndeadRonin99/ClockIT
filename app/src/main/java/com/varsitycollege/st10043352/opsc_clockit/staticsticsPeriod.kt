package com.varsitycollege.st10043352.opsc_clockit

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.properties.Delegates

class staticsticsPeriod : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private var startDate: Date? = null
    private var endDate: Date? = null
    private var startDateMillis by Delegates.notNull<Long>()
    private var endDateMillis by Delegates.notNull<Long>()
    private var currentActivities : MutableList<String> = mutableListOf("")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staticstics_period)
        sharedPreferences = getSharedPreferences("CategoryPreferences", MODE_PRIVATE)
        currentActivities.clear()


        // Retrieve the passed start and end dates from the intent
        startDateMillis = intent.getLongExtra("startDate", -1)
        endDateMillis = intent.getLongExtra("endDate", -1)

        // Convert the date millis to Date objects
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        startDate = if (startDateMillis != -1L) Date(startDateMillis) else null
        endDate = if (endDateMillis != -1L) Date(endDateMillis) else null

        // Find TextViews in the layout
        val txtStartDate = findViewById<TextView>(R.id.txtstartdate)
        val txtEndDate = findViewById<TextView>(R.id.txtenddate)

        // Display the selected start and end dates in the TextViews
        txtStartDate.text = "Start Date: ${startDate.let { dateFormat.format(it) } ?: "Not selected"}"
        txtEndDate.text = "End Date: ${endDate?.let { dateFormat.format(it) } ?: "Not selected"}"

        val backButton = findViewById <ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            // Navigate back to the home page
            val intent = Intent(this, stats::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Clear the back stack
            startActivity(intent)
            finish() // Finish current activity
        }
    }

    override fun onResume() {
        super.onResume()
        sharedPreferences = getSharedPreferences("CategoryPreferences", MODE_PRIVATE)
        val allActivities = sharedPreferences.all
        currentActivities.clear()

        // Remove previously added TextViews
        (findViewById<LinearLayout>(R.id.LinearActivities)).removeAllViews()

        // Iterate through all activities and create TextViews
        for ((key, value) in allActivities) {
            if(key.startsWith("Log_")) {
                val log = value as String
                val logData = formatLogs(log)

                // Get the date in "DD/MM" format from the log data
                val logDate = logData[4]
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                val logDateWithYear = "$logDate/$currentYear"

                // Convert the log date to a Date object
                val logDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val logDateFormatted = logDateFormat.parse(logDateWithYear)

                // Check if the log date falls between the selected start and end dates
                if (startDate != null && endDate != null && logDateFormatted != null) {
                    if (logDateFormatted >= startDate && logDateFormatted <= endDate) {
                        for ((key1, value) in allActivities) {
                            if (key1.startsWith("activity_")) { // Check if the key represents an activity
                                val activityData = value as String // Assuming the value is stored as a String
                                val activityTextView = TextView(this)
                                val activityInfo = formatActivity(activityData)
                                if(!currentActivities.contains(activityInfo[0])) {
                                    if (logData[0].equals(activityInfo[0])) {
                                        currentActivities.add(activityInfo[0])

                                        activityTextView.text = formatSharedPref(activityData)
                                        activityTextView.setTextColor(Color.WHITE)
                                        activityTextView.setTextSize(20f)
                                        activityTextView.setBackgroundResource(R.drawable.round_buttons)
                                        activityTextView.textAlignment =
                                            View.TEXT_ALIGNMENT_TEXT_START
                                        activityTextView.visibility =
                                            View.VISIBLE // Make the TextView visible
                                        activityTextView.layoutParams = LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            resources.getDimensionPixelSize(R.dimen.activity_box_height)
                                        ).apply {
                                            setMargins(
                                                resources.getDimensionPixelSize(R.dimen.activity_box_margin_start),
                                                resources.getDimensionPixelSize(R.dimen.activity_box_margin_top),
                                                resources.getDimensionPixelSize(R.dimen.activity_box_margin_end),
                                                resources.getDimensionPixelSize(R.dimen.activity_box_margin_bottom)
                                            )
                                        }
                                        activityTextView.setOnClickListener {
                                            val intent = Intent(this, PeriodLogged::class.java)
                                            intent.putExtra("activityData", activityData)
                                            intent.putExtra("startDate", startDateMillis)
                                            intent.putExtra("endDate", endDateMillis)
                                            startActivity(intent)
                                        }

                                        (findViewById<LinearLayout>(R.id.LinearActivities)).addView(
                                            activityTextView
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun formatSharedPref(activity: String?): CharSequence? {
        var activityDetails = ""
        activity?.let {
            val values = activity.split(",")

            val name = values[0]

            activityDetails= "\t\t${name}"
        }
        return activityDetails
    }

    fun formatLogs(log: String?): List<String> {
        var logData: List<String> = emptyList()

        log?.let {
            logData = log.split(",")
        }

        return logData
    }

    fun formatActivity(log: String?): List<String> {
        var logData: List<String> = emptyList()

        log?.let {
            logData = log.split(",")
        }

        return logData
    }

}

package com.varsitycollege.st10043352.opsc_clockit

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
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

class PeriodLogged : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private var startDate: Date? = null
    private var endDate: Date? = null
    private var startDateMillis by Delegates.notNull<Long>()
    private var endDateMillis by Delegates.notNull<Long>()
    private lateinit var activityList: List<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_period_logged)

        sharedPreferences = getSharedPreferences("CategoryPreferences", MODE_PRIVATE)
        val allActivities = sharedPreferences.all
        startDateMillis = intent.getLongExtra("startDate", -1)
        endDateMillis = intent.getLongExtra("endDate", -1)

        // Convert the date millis to Date objects
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        startDate = if (startDateMillis != -1L) Date(startDateMillis) else null
        endDate = if (endDateMillis != -1L) Date(endDateMillis) else null


        for ((key1, value) in allActivities) {
            if (key1.startsWith("Log_")) { // Check if the key represents an activity
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
                        //val activity = intent.getStringExtra()
                        val activityData = intent.getStringExtra("activityData")
                        activityList = formatActivities(activityData)
                        if(logData[0].equals(activityList[0])) {
                            val logData1 =
                                value as String // Assuming the value is stored as a String
                            val activityTextView = TextView(this)

                            activityTextView.text = formatSharedPref(logData1)
                            activityTextView.setTextColor(Color.WHITE)
                            activityTextView.setTextSize(20f)
                            activityTextView.setBackgroundResource(R.drawable.round_buttons)
                            activityTextView.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
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
                            activityTextView.setOnClickListener {//NEED TO CHANGE THIS TO THE NEW PAGE
                                val intent = Intent(this, ViewLog::class.java)
                                intent.putExtra("logData", logData1)

                                intent.putExtra("activityData", activityData)
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

    fun back2(view: View){
        finish()
    }

}
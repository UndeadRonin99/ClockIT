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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.properties.Delegates

class CategoryActivityInsights : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private var startDate: Date? = null
    private var endDate: Date? = null
    private var startDateMillis by Delegates.notNull<Long>()
    private var endDateMillis by Delegates.notNull<Long>()
    private var currentActivities: MutableList<String> = mutableListOf("")
    private var currentCategories: MutableList<String> = mutableListOf("")
    private var times: MutableList<String> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_insights)

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
        txtStartDate.text =
            "Start Date: ${startDate.let { dateFormat.format(it) } ?: "Not selected"}"
        txtEndDate.text = "End Date: ${endDate?.let { dateFormat.format(it) } ?: "Not selected"}"

        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            // Navigate back to the home page
            val intent = Intent(this, Insights::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Clear the back stack
            startActivity(intent)
            finish() // Finish current activity
        }
    }


    override fun onResume() {
        super.onResume()
        sharedPreferences = getSharedPreferences("CategoryPreferences", MODE_PRIVATE)
        val allActivities = sharedPreferences.all

        // Remove previously added TextViews
        (findViewById<LinearLayout>(R.id.LinearActivities2)).removeAllViews()

        // Iterate through all Categories and calculate hours logged for each within the selected period
        val categoryTimeMap = mutableMapOf<String, Pair<Int, Int>>() // Pair of (hours, minutes)
        for ((key, value) in allActivities) {
            if (key.startsWith("activity_")) {
                val activityData = value as String
                val activityInfo = formatActivity(activityData)
                val categoryName = activityInfo[2]

                // Initialize total hours and minutes for the category
                var totalHours = 0
                var totalMinutes = 0

                // Iterate through all logs to calculate total hours for the current category within the selected period
                for ((logKey, logValue) in allActivities) {
                    if (logKey.startsWith("Log_")) {
                        val log = logValue as String
                        val logData = formatLogs(log)
                        val logDate = logData[4]
                        val logDateWithYear =
                            "$logDate/${Calendar.getInstance().get(Calendar.YEAR)}"
                        val logDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val logDateFormatted = logDateFormat.parse(logDateWithYear)

                        // Check if the log date falls within the selected period
                        if (startDate != null && endDate != null && logDateFormatted != null) {
                            if (logDateFormatted >= startDate && logDateFormatted <= endDate) {
                                val times = logData[3].split(",")
                                for (tme in times) {
                                    val timeComponents = tme.split(":")
                                    val hours = timeComponents[0].toInt()
                                    val minutes = timeComponents[1].toInt()
                                    if (logData[1] == categoryName) {
                                        totalMinutes += hours * 60 + minutes
                                    }
                                }
                            }
                        }
                    }
                }

                // Calculate total hours and remaining minutes for the current category
                totalHours = totalMinutes / 60
                totalMinutes %= 60

                // Add the category and its total hours to the map
                categoryTimeMap[categoryName] = Pair(totalHours, totalMinutes)
            }
        }

        // Iterate through the categories and display only those with logged activities within the selected period
        for ((categoryName, categoryTime) in categoryTimeMap) {
            val hours = categoryTime.first
            val minutes = categoryTime.second

            // Check if hours and minutes are both 0, if so, skip adding the TextView
            if (hours == 0 && minutes == 0) {
                continue
            }

            // Create TextView for the current category
            val CategoryTextView = TextView(this)
            CategoryTextView.text = "$categoryName\t\t\t\t$hours hours $minutes minutes"
            CategoryTextView.setTextColor(Color.WHITE)
            CategoryTextView.setTextSize(20f)
            CategoryTextView.setBackgroundResource(R.drawable.round_buttons)
            CategoryTextView.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
            CategoryTextView.visibility = View.VISIBLE
            CategoryTextView.layoutParams = LinearLayout.LayoutParams(
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

            // Add the TextView to the appropriate LinearLayout
            (findViewById<LinearLayout>(R.id.LinearActivities2)).addView(CategoryTextView)
        }








        // Iterate through all activities and create TextViews
        for ((key, value) in allActivities) {
            if (key.startsWith("Log_")) {
                val log = value as String
                val logData = formatLogs(log)
                val logDate = logData[4]
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                val logDateWithYear = "$logDate/$currentYear"
                val logDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val logDateFormatted = logDateFormat.parse(logDateWithYear)

                if (startDate != null && endDate != null && logDateFormatted != null) {
                    if (logDateFormatted >= startDate && logDateFormatted <= endDate) {
                        for ((key1, value) in allActivities) {
                            if (key1.startsWith("activity_")) {
                                val activityData = value as String
                                val activityTextView = TextView(this)
                                val activityInfo = formatActivity(activityData)
                                if (!currentActivities.contains(activityInfo[0])) {
                                    if (logData[0].equals(activityInfo[0])) {
                                        currentActivities.add(activityInfo[0])

                                        activityTextView.text = formatSharedPref(activityData)
                                        activityTextView.setTextColor(Color.WHITE)
                                        activityTextView.setTextSize(20f)
                                        activityTextView.setBackgroundResource(R.drawable.round_buttons)
                                        activityTextView.textAlignment =
                                            View.TEXT_ALIGNMENT_TEXT_START
                                        activityTextView.visibility =
                                            View.VISIBLE
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
                                            val intent = Intent(this, ActivityInfo1::class.java)
                                            intent.putExtra("activityName", activityInfo[0])
                                            intent.putExtra("description", activityInfo[1])
                                            intent.putExtra("category", activityInfo[2])
                                            intent.putExtra("startTime", activityInfo[4])
                                            intent.putExtra("endTime", activityInfo[5])
                                            intent.putExtra("photoUri", activityInfo[6])
                                            startActivity(intent)
                                        }


                                        (findViewById<LinearLayout>(R.id.LinearActivities1)).addView(
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

    fun formatCategory(activity: String?): CharSequence? {
        var activityDetails = ""
        activity?.let {
            val values = activity.split(",")

            val name = values[2]

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
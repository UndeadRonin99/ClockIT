package com.varsitycollege.st10043352.opsc_clockit

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.properties.Delegates

class PeriodLogged : AppCompatActivity() {
    private var SessionData: Map<String, Any>? = null
    private lateinit var ActivityData: Map<String, Any>
    private val database = FirebaseDatabase.getInstance("https://clockit-13d02-default-rtdb.europe-west1.firebasedatabase.app")
    private lateinit var allActivities: Map<String, Any>
    private var activityList: List<String> = mutableListOf("")
    private lateinit var photos: Array<String>
    private var color : Int = 0

    private lateinit var sharedPreferences: SharedPreferences
    private var startDate: Date? = null
    private var endDate: Date? = null
    private var startDateMillis by Delegates.notNull<Long>()
    private var endDateMillis by Delegates.notNull<Long>()
    private var activityName : String? = null
    private var category : String? = null
    private var photo : String? = null
    private var minGoal: Float = 0f
    private var maxGoal: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_period_logged)

        activityName = intent.getStringExtra("activityName")
        category = intent.getStringExtra("category")
        color = (intent.getStringExtra("color"))?.toInt() ?: 0

        val txtActivity = findViewById<TextView>(R.id.actName)
        val txtCategory = findViewById<TextView>(R.id.Category)

        txtActivity.text = activityName
        txtCategory.text = category
        Log.e("color","$color")
        if (color != null) {
            txtCategory.setTextColor(color.toInt())
        }

        sharedPreferences = getSharedPreferences("CategoryPreferences", MODE_PRIVATE)
        startDateMillis = intent.getLongExtra("startDate", -1)
        endDateMillis = intent.getLongExtra("endDate", -1)

        // Convert the date millis to Date objects
        startDate = if (startDateMillis != -1L) Date(startDateMillis) else null
        endDate = if (endDateMillis != -1L) Date(endDateMillis) else null

        // Fetch daily goals
        fetchGoalsForActivity(activityName ?: "", findViewById(R.id.textView11))

    }

    private fun setupChart(logsData: List<Float>, minGoal: Float, maxGoal: Float) {
        val barChart = findViewById<BarChart>(R.id.barChart)

        val labels = logsData.indices.map { (it + 1).toString() }

        val entries = logsData.mapIndexed { index, hours -> BarEntry(index.toFloat(), hours) }
        val dataSet = BarDataSet(entries, "Logs")
        dataSet.color = color

        val data = BarData(dataSet)
        barChart.data = data

        // Customize x-axis
        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)
        xAxis.granularity = 1f
        xAxis.textColor = Color.WHITE

        // Customize y-axis
        val yAxisRight = barChart.axisRight
        yAxisRight.isEnabled = false

        val yAxisLeft = barChart.axisLeft
        yAxisLeft.setDrawGridLines(false)
        yAxisLeft.textColor = Color.WHITE

        // Add limit lines for min and max goals
        val llMin = LimitLine(minGoal, "Min Goal")
        llMin.lineWidth = 2f
        llMin.lineColor = Color.RED
        llMin.textColor = Color.WHITE
        llMin.textSize = 12f

        val llMax = LimitLine(maxGoal, "Max Goal")
        llMax.lineWidth = 2f
        llMax.lineColor = Color.GREEN
        llMax.textColor = Color.WHITE
        llMax.textSize = 12f

        yAxisLeft.addLimitLine(llMin)
        yAxisLeft.addLimitLine(llMax)

        // Refresh chart
        barChart.invalidate()
    }

    override fun onResume() {
        super.onResume()
        photos = emptyArray()
        fetchActivitiesFromFirebaseDatabase()
        fetchSessionsFromFirebaseDatabase()
    }

    private fun updateUI() {
        val linearLayout = findViewById<LinearLayout>(R.id.LinearActivities1)
        linearLayout.removeAllViews()

        allActivities = ActivityData
        var allSessions = SessionData

        // List to hold the hours for the chart
        val logsData = mutableListOf<Float>()

        // Iterate through all activities + sessions and create TextViews
        if (allSessions != null) {
            for ((sessionKey, sessionValue) in allSessions) {
                val logMap = mutableMapOf<String, Any>()
                logMap[sessionKey] = sessionValue

                val log = formatSessionFirebaseData(logMap, sessionKey)
                val logData = formatLogs(log as String)

                if (logData[4] != "null") {
                    var logDate = logData[3]
                    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                    val logDateWithYear = "$logDate/$currentYear"
                    val logDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val logDateFormatted = logDateFormat.parse(logDateWithYear)

                    if (startDate != null && endDate != null && logDateFormatted != null) {
                        if (logDateFormatted >= startDate && logDateFormatted <= endDate) {
                            if (logData[0].equals(intent.getStringExtra("activityName"))) {

                                val activityTextView = TextView(this)

                                val time = logData[5]
                                val (hoursString, minutesString) = time.split(":")
                                val hours = hoursString.toInt()
                                val minutes = minutesString.toInt()
                                val totalHours = hours + minutes / 60.0f
                                logsData.add(totalHours)
                                val formattedTime = "${hours} Hours ${minutes} Minutes"

                                photo = logData[4]
                                photos += ("$photo,$formattedTime,$activityName")

                                activityTextView.text = formatSharedPref(formattedTime)
                                activityTextView.setTextColor(Color.WHITE)
                                activityTextView.setTextSize(20f)
                                activityTextView.setBackgroundResource(R.drawable.round_buttons)
                                activityTextView.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                                activityTextView.visibility = View.VISIBLE
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
                                    val intent = Intent(this, ViewLog::class.java)
                                    intent.putExtra("photos", photos)
                                    intent.putExtra("activityName", activityName)
                                    intent.putExtra("category", category)
                                    intent.putExtra("time", formattedTime)

                                    startActivity(intent)
                                }

                                linearLayout.addView(activityTextView)
                            }
                        }
                    }
                }
            }
        }

        // Setup the chart with the logsData
        setupChart(logsData, minGoal, maxGoal)
    }

    @SuppressLint("RestrictedApi")
    private fun fetchActivitiesFromFirebaseDatabase() {
        val activitiesRef = database.getReference("activities")
        Log.d("CategoryActivityInsights", "Database reference: ${activitiesRef.path}")

        activitiesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    ActivityData = snapshot.children.associate { it.key!! to it.value as Any }
                    Log.d("ActivityData", "Activities data retrieved: $ActivityData")
                    updateUI()
                } else {
                    Log.d("CategoryActivityInsights", "No data found at the reference path")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("CategoryActivityInsights", "Database error: ${error.message}")
            }
        })
        Log.d("CategoryActivityInsights", "Listener attached to database reference")
    }

    @SuppressLint("RestrictedApi")
    private fun fetchSessionsFromFirebaseDatabase() {
        val activitiesRef = database.getReference("logged_sessions")
        Log.d("CategoryActivityInsights", "Database reference: ${activitiesRef.path}")

        activitiesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    SessionData = snapshot.children.associate { it.key!! to it.value as Any }
                    Log.d("ActivityData", "Activities data retrieved: $SessionData")
                    updateUI()
                } else {
                    Log.d("CategoryActivityInsights", "No data found at the reference path")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("CategoryActivityInsights", "Database error: ${error.message}")
            }
        })
        Log.d("CategoryActivityInsights", "Listener attached to database reference")
    }

    private fun formatActivityFirebaseData(activityData: Map<String, Any>): CharSequence {
        val name = activityData["activityName"]
        val category = activityData["categoryName"]
        val color = activityData["color"]
        val description = activityData["description"]
        val endTime = activityData["endTime"]
        val startTime = activityData["startTime"]
        val photoUrl = activityData["photoUrl"]
        return "$name,$category,$color,$description,$endTime,$startTime,$photoUrl"
    }

    private fun formatSessionFirebaseData(sessionData: Map<String, Any>, sessionKey: String): CharSequence {
        val nestedMap = sessionData[sessionKey] as? Map<String, Any>

        val name = nestedMap?.get("activityName") as? String ?: ""
        val category = nestedMap?.get("categoryName") as? String ?: ""
        val color = nestedMap?.get("categoryColor") as? String ?: ""
        val date = nestedMap?.get("date") as? String ?: ""
        val photoUri = nestedMap?.get("imageUrl") as? String ?: ""
        val time = nestedMap?.get("time") as? String ?: ""

        return "$name,$category,$color,$date,$photoUri,$time"
    }

    private fun fetchGoalsForActivity(activityName: String, dailyGoalsTextView: TextView) {
        val goalsRef = database.getReference("goals")
        goalsRef.child(activityName).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val minGoalStr = snapshot.child("min_goal").getValue(String::class.java)
                val maxGoalStr = snapshot.child("max_goal").getValue(String::class.java)

                val dailyGoalsText = StringBuilder()
                if (!minGoalStr.isNullOrEmpty()) {
                    dailyGoalsText.append("Min goal: $minGoalStr\t\t\t\t\t")
                }

                if (!maxGoalStr.isNullOrEmpty()) {
                    dailyGoalsText.append("Max goal: $maxGoalStr")
                }
                if (minGoalStr.isNullOrEmpty() && maxGoalStr.isNullOrEmpty()) {
                    dailyGoalsText.append("No goals have been set")
                }

                dailyGoalsTextView.text = dailyGoalsText.toString()
                minGoal = minGoalStr?.toFloatOrNull() ?: 0f
                maxGoal = maxGoalStr?.toFloatOrNull() ?: 0f
                setupChart(emptyList(), minGoal, maxGoal)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FetchGoals", "Error fetching goals: ${error.message}")
            }
        })
    }

    fun formatSharedPref(activity: String?): CharSequence? {
        var activityDetails = ""
        activity?.let {
            val values = activity.split(",")

            val name = values[0]

            activityDetails = "\t\t${name}"
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

    fun back2(view: View) {
        finish()
    }
}

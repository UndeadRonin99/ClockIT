
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.w3c.dom.Text
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


    private lateinit var sharedPreferences: SharedPreferences
    private var startDate: Date? = null
    private var endDate: Date? = null
    private var startDateMillis by Delegates.notNull<Long>()
    private var endDateMillis by Delegates.notNull<Long>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_period_logged)

        val activityName = intent.getStringExtra("activityName")
        val category = intent.getStringExtra("category")
        val color = intent.getStringExtra("color")

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



    }

    override fun onResume() {
        super.onResume()
        fetchActivitiesFromFirebaseDatabase()
        fetchSessionsFromFirebaseDatabase()
    }

    private fun updateUI() {
        // Remove previously added TextViews
        //findViewById<LinearLayout>(R.id.LinearActivities1).removeAllViews()

        allActivities = ActivityData
        var allSessions = SessionData

        // Remove previously added TextViews
        //findViewById<LinearLayout>(R.id.LinearActivities2).removeAllViews()



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

                    if (startDate != null && endDate != null && logDateFormatted != null) if (startDate != null && endDate != null && logDateFormatted != null) {
                        if (logDateFormatted >= startDate && logDateFormatted <= endDate) {
                            //val activity = intent.getStringExtra()

                            if (logData[0].equals(intent.getStringExtra("activityName"))) {

                                val activityTextView = TextView(this)

                                val time = logData[5]
                                val (hoursString, minutesString) = time.split(":")
                                val hours = hoursString.toInt()
                                val minutes = minutesString.toInt()
                                val formattedTime = "${hours} Hours ${minutes} Minutes"


                                activityTextView.text = formatSharedPref(formattedTime)
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
                                    intent.putExtra("activityName", intent.getStringExtra("activityName"))

                                    //intent.putExtra("Category",)
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

    @SuppressLint("RestrictedApi")
    private fun fetchActivitiesFromFirebaseDatabase() {
        val activitiesRef = database.getReference("activities")
        Log.d("CategoryActivityInsights", "Database reference: ${activitiesRef.path}")  // Log the database reference path

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
        Log.d("CategoryActivityInsights", "Listener attached to database reference")  // Confirm the listener is attached
    }

    @SuppressLint("RestrictedApi")
    private fun fetchSessionsFromFirebaseDatabase() {
        val activitiesRef = database.getReference("logged_sessions")
        Log.d("CategoryActivityInsights", "Database reference: ${activitiesRef.path}")  // Log the database reference path

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
        Log.d("CategoryActivityInsights", "Listener attached to database reference")  // Confirm the listener is attached
    }

    private fun formatActivityFirebaseData(activityData: Map<String, Any>): CharSequence {
        // Format the activity data as needed
        // Example:
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
        // Access the nested Map using the session key
        val nestedMap = sessionData[sessionKey] as? Map<String, Any>

        // Access values using safe casting
        val name = nestedMap?.get("activityName") as? String ?: ""
        val category = nestedMap?.get("categoryName") as? String ?: ""
        val color = nestedMap?.get("categoryColor") as? String ?: ""
        val date = nestedMap?.get("date") as? String ?: ""
        val photoUri = nestedMap?.get("imageUrl") as? String ?: ""
        val time = nestedMap?.get("time") as? String ?: ""

        return "$name,$category,$color,$date,$photoUri,$time"
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


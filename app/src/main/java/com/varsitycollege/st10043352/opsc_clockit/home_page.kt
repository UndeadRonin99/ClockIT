package com.varsitycollege.st10043352.opsc_clockit

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

class home_page : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var txtActivity: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        val sharedPreferences = getSharedPreferences("CategoryPreferences", MODE_PRIVATE)
        val allActivities = sharedPreferences.all
        for ((key, value) in allActivities) {
            Log.d("SharedPreferences", "Key: $key, Value: $value")
        }
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences("CategoryPreferences", MODE_PRIVATE)
        val allActivities = sharedPreferences.all

        // Remove previously added TextViews
        (findViewById<LinearLayout>(R.id.LinearActivities)).removeAllViews()

        // Iterate through all activities and create TextViews
        for ((key, value) in allActivities) {
            if (key.startsWith("activity_")) { // Check if the key represents an activity
                val activityData = value as String // Assuming the value is stored as a String
                val activityTextView = TextView(this)

                activityTextView.text = formatSharedPref(activityData)
                activityTextView.setTextColor(Color.WHITE)
                activityTextView.setTextSize(20f)
                activityTextView.setBackgroundResource(R.drawable.round_buttons)
                activityTextView.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                activityTextView.visibility = View.VISIBLE // Make the TextView visible
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
                    val intent = Intent(this, LogHours::class.java)
                    intent.putExtra("activityData", activityData)
                    startActivity(intent)
                }

                (findViewById<LinearLayout>(R.id.LinearActivities)).addView(activityTextView)
            }
        }
    }

    fun formatSharedPref(activity: String?): CharSequence? {
        var activityDetails = ""
        activity?.let {
            val values = activity.split(",")

            val name = values[0]
            val description = values[1]
            val category = values[2]
            val color = values[3]
            val number1 = values[4]
            val number2 = values[5]
            val contentUri = values[6]

            // Now you can use these variables as needed
            // For example:
            activityDetails= "${name}\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t${category}\n${number1}\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t${number2}"
        }
        return activityDetails
    }



    fun navAddCategory(view: View) {
        startActivity(Intent(this, Add_Category::class.java))
    }

    fun navAddActivity(view: View) {
        startActivity(Intent(this, AddActivity::class.java))
    }

    fun navInsights(view: View) {
        startActivity(Intent(this, Insights::class.java))
    }

    fun navStats(view: View){
        startActivity(Intent(this, stats::class.java))
    }

    fun navGoals(view: View){
        startActivity(Intent(this, goals::class.java))
    }

    fun navFun(view: View){
        startActivity(Intent(this, FunTime::class.java))
    }
}
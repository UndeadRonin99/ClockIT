package com.varsitycollege.st10043352.opsc_clockit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class Insights : AppCompatActivity() {

    private lateinit var startDate: TextView
    private lateinit var endDate: TextView
    private lateinit var calendarView: CalendarView
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    private var selectedStartDate: Date? = null
    private var selectedEndDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insights)

        startDate = findViewById(R.id.StartDate)
        endDate = findViewById(R.id.EndDate)
        calendarView = findViewById(R.id.calendarView)

        // Set listener for calendar view
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = getDateFromDatePicker(year, month, dayOfMonth)
            if (selectedStartDate == null || selectedEndDate != null) {
                // Set the selected date as start date if no start date is selected or if end date is already selected
                selectedStartDate = selectedDate
                selectedEndDate = null
                startDate.text = "Start Date: ${dateFormat.format(selectedDate)}"
                endDate.text = ""
            } else if (selectedStartDate != null && selectedEndDate == null) {
                // Set the selected date as end date if start date is already selected but end date is not
                selectedEndDate = selectedDate
                endDate.text = "End Date: ${dateFormat.format(selectedDate)}"
            }
        }
    }

    // Function to get Date from DatePicker
    private fun getDateFromDatePicker(year: Int, month: Int, day: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar.time
    }

    fun navFun(view: View) {
        startActivity(Intent(this, FunTime::class.java))
    }

    fun navStats(view: View){
        startActivity(Intent(this, stats::class.java))
    }

    fun navGoals(view: View){
        startActivity(Intent(this, goals::class.java))
    }

    fun navHome(view: View){
        startActivity(Intent(this, home_page::class.java))
    }
}

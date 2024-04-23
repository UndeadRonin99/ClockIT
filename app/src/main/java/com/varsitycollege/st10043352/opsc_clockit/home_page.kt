package com.varsitycollege.st10043352.opsc_clockit

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

class home_page : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences


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
        setContentView(R.layout.activity_home_page)
        val sharedPreferences = getSharedPreferences("CategoryPreferences", MODE_PRIVATE)
        val allActivities = sharedPreferences.all
        for ((key, value) in allActivities) {
            Log.d("SharedPreferences", "Key: $key, Value: $value")
        }
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
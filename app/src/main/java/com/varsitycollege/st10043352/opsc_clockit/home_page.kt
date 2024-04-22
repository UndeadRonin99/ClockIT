package com.varsitycollege.st10043352.opsc_clockit

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class home_page : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
    }

    private fun navAddActivity() {
    }

    fun navAddCategory(view: View) {
        startActivity(Intent(this, Add_Category::class.java))
    }

    fun navAddActivity(view: View) {
        startActivity(Intent(this, AddActivity::class.java))
    }
}
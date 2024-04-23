package com.varsitycollege.st10043352.opsc_clockit

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Insights : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insights)
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
package com.varsitycollege.st10043352.opsc_clockit

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import androidx.core.content.ContextCompat
import android.view.View
import android.view.ViewGroup

class LeaderBoard : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var activityRef: DatabaseReference
    private lateinit var leaderboardContainer: LinearLayout
    private val botUsers = listOf("bot1", "bot2", "bot3", "bot4", "bot5")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leader_board)

        database = FirebaseDatabase.getInstance("https://clockit-13d02-default-rtdb.europe-west1.firebasedatabase.app")
        activityRef = database.getReference("logged_sessions")

        leaderboardContainer = findViewById(R.id.leaderboardContainer)

        fetchLeaderboardData()
    }

    private fun fetchLeaderboardData() {
        activityRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var totalUserPoints = 0
                val botPointsMap: MutableMap<String, Int> = mutableMapOf()

                for (snapshot in dataSnapshot.children) {
                    val session = snapshot.getValue(LoggedSession::class.java)
                    session?.let {
                        val totalMinutes = convertToMinutes(it.time)
                        if (!botUsers.contains(it.activityName.toLowerCase())) {
                            totalUserPoints += totalMinutes
                        } else {
                            botPointsMap[it.activityName] = botPointsMap.getOrDefault(it.activityName, 0) + totalMinutes
                        }
                    }
                }

                // Adding bot users with fixed points for illustration
                botPointsMap["bot1"] = 2019
                botPointsMap["bot2"] = 1431
                botPointsMap["bot3"] = 1241
                botPointsMap["bot4"] = 1051
                botPointsMap["bot5"] = 432

                val userPointsMap = mutableMapOf("Me" to totalUserPoints)
                userPointsMap.putAll(botPointsMap)

                val sortedUserPoints = userPointsMap.entries.sortedByDescending { it.value }
                displayLeaderboard(sortedUserPoints)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@LeaderBoard, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun convertToMinutes(time: String): Int {
        val parts = time.split(":")
        val hours = parts[0].toInt()
        val minutes = parts[1].toInt()
        return hours * 60 + minutes
    }

    private fun displayLeaderboard(userPointsList: List<Map.Entry<String, Int>>) {
        leaderboardContainer.removeAllViews() // Clear existing views if any

        for ((index, entry) in userPointsList.withIndex()) {
            val userLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 8, 0, 8)
                }
            }

            val userRankTextView = TextView(this).apply {
                text = "${index + 1}"
                setTextColor(ContextCompat.getColor(context, android.R.color.white))
                textSize = 18f
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.2f)
            }

            val userNameTextView = TextView(this).apply {
                text = entry.key
                setTextColor(ContextCompat.getColor(context, android.R.color.white))
                textSize = 18f
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.4f)
            }

            val userPointsTextView = TextView(this).apply {
                text = "${entry.value} pts"
                setTextColor(ContextCompat.getColor(context, android.R.color.white))
                textSize = 18f
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.4f)
            }

            userLayout.addView(userRankTextView)
            userLayout.addView(userNameTextView)
            userLayout.addView(userPointsTextView)

            leaderboardContainer.addView(userLayout)
        }
    }

    fun back(view: View) {
        finish()
    }
}

data class LoggedSession(
    val activityName: String = "",
    val categoryName: String = "",
    val categoryColor: String = "",
    val time: String = "",
    val date: String = "",
    val imageUrl: String = ""
)

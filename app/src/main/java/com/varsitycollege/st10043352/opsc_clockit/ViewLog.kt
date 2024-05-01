package com.varsitycollege.st10043352.opsc_clockit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ViewLog : AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var txtName: TextView
    private lateinit var txtCategory: TextView
    private lateinit var txtTime: TextView
    private lateinit var LogPhoto: ImageView
    private var logList: List<String> = listOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_log)

        backButton = findViewById(R.id.back_button)
        txtName = findViewById(R.id.ActivityName)
        txtCategory = findViewById(R.id.CategoryName)
        txtTime = findViewById(R.id.TimeOnTask)
        LogPhoto = findViewById(R.id.LogPhoto)

        val activityData = intent.getStringExtra("activityData")
        val logData = intent.getStringExtra("logData")

        val activityList = formatActivities(activityData)
        logList = formatLogs(logData)
        val logTime = formatSharedPref(logData)

        txtName.text = activityList.getOrNull(0) ?: "Activity Name Not Found"
        txtCategory.text = activityList.getOrNull(2) ?: "Category Not Found"
        txtTime.text = logTime

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            loadLogImage(logList)
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_STORAGE_PERMISSION)
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadLogImage(logList)
            } else {
                showPermissionExplanationDialog()
            }
        }
    }

    private fun showPermissionExplanationDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Permission Required")
        dialogBuilder.setMessage("Storage permission is required to view the log image. Please grant the permission.")
        dialogBuilder.setPositiveButton("Grant") { dialog, _ ->
            openAppSettings()
            dialog.dismiss()
        }
        dialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun loadLogImage(logList: List<String>) {
        if (logList.size >= 6 && !logList[5].isNullOrEmpty()) {
            val logImageURI = Uri.parse(logList[5])
            LogPhoto.setImageURI(logImageURI)
        } else {
            Log.e("ViewLog", "URI string is null or invalid.")
        }
    }

    companion object {
        private const val REQUEST_STORAGE_PERMISSION = 10196
    }

    fun formatLogs(log: String?): List<String> {
        return log?.split(",") ?: emptyList()
    }

    fun formatActivities(log: String?): List<String> {
        return log?.split(",") ?: emptyList()
    }

    fun formatSharedPref(activity: String?): CharSequence? {
        return activity?.let {
            val values = activity.split(",")
            val name = values[3]
            val times = name.split(":")
            "\t${times[0]} hours ${times[1]} minutes"
        } ?: ""
    }
}

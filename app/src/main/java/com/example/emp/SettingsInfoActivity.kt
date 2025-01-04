package com.example.emp

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_info)

        val deleteAllDataButton: Button = findViewById(R.id.deleteAllData)

        deleteAllDataButton.setOnClickListener {
            // Handle the delete all data action
            // For now, show a simple toast message
            Toast.makeText(this, "All data deleted!", Toast.LENGTH_SHORT).show()
        }
    }
}

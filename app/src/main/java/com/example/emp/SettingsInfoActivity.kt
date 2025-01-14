package com.example.emp

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingsInfoActivity : AppCompatActivity() {
    private val sharedPreferences by lazy { getSharedPreferences("Palettes", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        applySavedTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_info)

        Log.d("SettingsInfoActivityLog", "onCreate called")

        val deleteAllDataButton: Button = findViewById(R.id.deleteAllData)
        deleteAllDataButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete all data?")
                .setPositiveButton("Yes") { _, _ ->
                    sharedPreferences.edit().clear().apply()
                    Toast.makeText(this, "All data deleted!", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        val spinner: Spinner = findViewById(R.id.tema)
        val options = arrayOf("Auto", "Day", "Night")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val savedTheme = sharedPreferences.getString("theme", "Auto")
        spinner.setSelection(
            when (savedTheme) {
                "Auto" -> 0
                "Day" -> 1
                "Night" -> 2
                else -> 0
            }
        )

        var isFirstSelection = true
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isFirstSelection) {
                    isFirstSelection = false
                    return
                }

                val selectedTheme = options[position]
                if (selectedTheme == savedTheme) return

                when (selectedTheme) {
                    "Auto" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    "Day" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    "Night" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }

                saveThemePreference(selectedTheme)
                recreate()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }

        val descriptionText = findViewById<TextView>(R.id.descriptionText)
        val description = """
        <b>MyPalette</b> is an open-source interactive color palette generator designed to help you create beautiful and harmonious color schemes with ease. 
        It empowers designers, developers, and creatives with intuitive tools to explore a wide range of color combinations and themes.  <br><br>

        Key features include the ability to <b>save</b>, <b>favorite</b>, and <b>share</b> your palettes effortlessly.<br><br>

        <b>Harmony modes:</b><br>
        """.trimIndent()

        descriptionText.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(description)
        }

        val cameraButton = findViewById<ImageButton>(R.id.cameraButton)
        val homeButton = findViewById<ImageButton>(R.id.homeButton)
        val viewOnlinePalletsButton = findViewById<ImageButton>(R.id.viewOnlinePalletesButton)
        val viewSavedPalettesButton = findViewById<ImageButton>(R.id.viewSavedPalettesButton)

        cameraButton.setOnClickListener {
            val intent = Intent(this, FromPhotoActivity::class.java)
            startActivity(intent)
        }

        viewSavedPalettesButton.setOnClickListener {
            val intent = Intent(this, SavedPalettesActivity::class.java)
            startActivity(intent)
        }

        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        viewOnlinePalletsButton.setOnClickListener {
            val intent = Intent(this, OnlinePallets::class.java)
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
        Log.d("SettingsInfoActivityLog", "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("SettingsInfoActivityLog", "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("SettingsInfoActivityLog", "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("SettingsInfoActivityLog", "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("SettingsInfoActivityLog", "onDestroy called")
    }

    private fun applySavedTheme() {
        val savedTheme = sharedPreferences.getString("theme", "Auto")
        when (savedTheme) {
            "Auto" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            "Day" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "Night" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun saveThemePreference(theme: String) {
        sharedPreferences.edit().putString("theme", theme).apply()
    }
}

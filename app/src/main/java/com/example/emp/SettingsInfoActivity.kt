package com.example.emp

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.text.Html
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
        val options = arrayOf("Night", "Day")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Retrieve saved theme from SharedPreferences
        val savedTheme = sharedPreferences.getString("theme", "Night")
        var isFirstSelection = true

        spinner.setSelection(if (savedTheme == "Night") 0 else 1)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isFirstSelection) {
                    isFirstSelection = false
                    return
                }

                val selectedTheme = options[position]
                if (selectedTheme == savedTheme) return

                when (position) {
                    0 -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        sharedPreferences.edit().putString("theme", "Night").apply()
                    }
                    1 -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        sharedPreferences.edit().putString("theme", "Day").apply()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Description text
        val descriptionText = findViewById<TextView>(R.id.descriptionText)
        val description = """
        <b>MyPalette</b> is an open-source interactive color palette generator designed to help you create beautiful and harmonious color schemes with ease. 
        It empowers designers, developers, and creatives with intuitive tools to explore a wide range of color combinations and themes. <br><br>
        
        Key features include the ability to <b>save</b>, <b>favorite</b>, and <b>share</b> your palettes effortlessly.<br><br>
        
        <b>Harmony modes:</b><br>
        """.trimIndent()

        descriptionText.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(description)
        }
    }

    private fun applySavedTheme() {
        val savedTheme = sharedPreferences.getString("theme", "Night")
        when (savedTheme) {
            "Night" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "Day" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}

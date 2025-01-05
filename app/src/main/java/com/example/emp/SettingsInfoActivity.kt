package com.example.emp

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingsInfoActivity : AppCompatActivity() {
    private val sharedPreferences by lazy { getSharedPreferences("Palettes", MODE_PRIVATE) }
    override fun onCreate(savedInstanceState: Bundle?) {
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
        val options = arrayOf("Avto", "Day", "Night") // Možnosti za spinner

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        var isChangingTheme = false

        fun avto() {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
        fun day() {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                Toast.makeText(this, "Day mode  (we hate it too)", Toast.LENGTH_SHORT).show()
        }
        fun night() {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                Toast.makeText(this, "Night mode", Toast.LENGTH_SHORT).show()
        }

        var isFirstSelection = true
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isFirstSelection) {
                    isFirstSelection = false
                    return
                }
                if (isChangingTheme) return
                when (position) {
                    0 -> avto()
                    1 -> day()
                    2 -> night()
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Nič ne naredite, ko ni izbranih nobenih vrednosti
            }
        }

        // Opis
        val descriptionText = findViewById<TextView>(R.id.descriptionText)
        val description = """
        <b>MyPalette</b> is an open-source interactive color palette generator designed to help you create beautiful and harmonious color schemes with ease. 
        It empowers designers, developers, and creatives with intuitive tools to explore a wide range of color combinations and themes.  <br><br>
        
        Key features include the ability to <b>save</b>, <b>favorite</b>, and <b>share</b> your palettes effortlessly.<br><br>
        
        <b>Harmony modes:</b><br>
    """.trimIndent()


        descriptionText.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY)
        } else
        {
            Html.fromHtml(description)
        }
    }
}

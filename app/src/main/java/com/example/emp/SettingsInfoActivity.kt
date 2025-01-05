package com.example.emp

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
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
            sharedPreferences.edit().clear().apply()
            Toast.makeText(this, "All data deleted!", Toast.LENGTH_SHORT).show()
        }


        val spinner: Spinner = findViewById(R.id.tema)
        val options = arrayOf("Avto", "Day", "Night") // Možnosti za spinner

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        fun avto() {
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Toast.makeText(this, "Avto mode", Toast.LENGTH_SHORT).show()           //  se ne dela, mora glede na moc svetlobe v okolici.
        }
        fun day() {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Toast.makeText(this, "Day mode  (we hate it too)", Toast.LENGTH_SHORT).show()
        }
        fun night() {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Toast.makeText(this, "Night mode", Toast.LENGTH_SHORT).show()
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: View?, position: Int, id: Long) {
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
    }
}

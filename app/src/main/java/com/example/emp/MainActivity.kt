package com.example.emp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import yuku.ambilwarna.AmbilWarnaDialog

class MainActivity : AppCompatActivity() {

    private var selectedColor: Int = Color.BLACK
    private val sharedPreferences by lazy { getSharedPreferences("Palettes", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val colorInput = findViewById<EditText>(R.id.colorInput)
        val schemeSpinner = findViewById<Spinner>(R.id.schemeSpinner)
        val generateButton = findViewById<Button>(R.id.generateButton)
        val colorPickerButton = findViewById<Button>(R.id.colorPickerButton)
        val savePaletteButton = findViewById<Button>(R.id.savePaletteButton)
        val viewSavedPalettesButton = findViewById<Button>(R.id.viewSavedPalettesButton)
        val paletteContainer = findViewById<LinearLayout>(R.id.paletteContainer)

        val schemes = listOf("Complementary", "Monochromatic", "Analogous", "Triadic", "Tetradic")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, schemes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        schemeSpinner.adapter = adapter

        colorInput.setText("#${Integer.toHexString(selectedColor).substring(2).uppercase()}")

        colorPickerButton.setOnClickListener { openColorPickerDialog() }

        generateButton.setOnClickListener {
            val hexColor = colorInput.text.toString().trim()

            if (!hexColor.matches(Regex("^#([A-Fa-f0-9]{6})$"))) {
                Toast.makeText(this, "Enter a valid HEX color (e.g., #FF5733)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val schemeType = schemeSpinner.selectedItem?.toString() ?: "Complementary"

            val palette = when (schemeType) {
                "Complementary" -> ColorUtils.generateComplementaryColor(hexColor)
                "Monochromatic" -> ColorUtils.generateMonochromaticColors(hexColor)
                "Analogous" -> ColorUtils.generateAnalogousColors(hexColor)
                "Triadic" -> ColorUtils.generateTriadicColors(hexColor)
                "Tetradic" -> ColorUtils.generateTetradicColors(hexColor)
                else -> listOf(hexColor)
            }

            displayPalette(paletteContainer, palette)
        }

        savePaletteButton.setOnClickListener {
            val currentPalette = (0 until paletteContainer.childCount).map {
                val view = paletteContainer.getChildAt(it)
                val color = view.tag as? String
                color ?: return@map null
            }.filterNotNull()

            if (currentPalette.isNotEmpty()) {
                savePaletteToPreferences(currentPalette)
                Toast.makeText(this, "Palette saved!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No palette to save!", Toast.LENGTH_SHORT).show()
            }
        }

        viewSavedPalettesButton.setOnClickListener {
            val intent = Intent(this, SavedPalettesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openColorPickerDialog() {
        val colorPickerDialog = AmbilWarnaDialog(this, selectedColor, object : AmbilWarnaDialog.OnAmbilWarnaListener {
            override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                selectedColor = color
                val hexColor = String.format("#%06X", (0xFFFFFF and color))
                findViewById<EditText>(R.id.colorInput).setText(hexColor)
            }

            override fun onCancel(dialog: AmbilWarnaDialog?) {}
        })
        colorPickerDialog.show()
    }

    private fun displayPalette(container: LinearLayout, colors: List<String>) {
        container.removeAllViews()
        for (color in colors) {
            val view = View(this)
            view.setBackgroundColor(Color.parseColor(color))
            view.tag = color
            val layoutParams = LinearLayout.LayoutParams(0, 200, 1f)
            layoutParams.setMargins(8, 0, 8, 0)
            view.layoutParams = layoutParams
            container.addView(view)
        }
    }

    private fun savePaletteToPreferences(palette: List<String>) {
        val editor = sharedPreferences.edit()
        val key = "palette_${System.currentTimeMillis()}"
        editor.putStringSet(key, palette.toSet())
        editor.putBoolean("liked_$key", false)
        editor.apply()
    }
}

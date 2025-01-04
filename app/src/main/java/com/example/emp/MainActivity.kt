package com.example.emp

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import yuku.ambilwarna.AmbilWarnaDialog

class MainActivity : AppCompatActivity() {
    private var selectedColor: Int = Color.BLACK
    private val sharedPreferences by lazy { getSharedPreferences("Palettes", MODE_PRIVATE) }
    private val colorHistory = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val colorInput = findViewById<EditText>(R.id.colorInput)
        val schemeSpinner = findViewById<Spinner>(R.id.schemeSpinner)
        val generateButton = findViewById<Button>(R.id.generateButton)
        val colorPickerButton = findViewById<Button>(R.id.colorPickerButton)
        val savePaletteButton = findViewById<Button>(R.id.savePaletteButton)
        val viewSavedPalettesButton = findViewById<Button>(R.id.viewSavedPalettesButton)
        val settings = findViewById<ImageButton>(R.id.settingsButton)
        val paletteContainer = findViewById<LinearLayout>(R.id.paletteContainer)
        val historyContainer = findViewById<LinearLayout>(R.id.historyContainer)  // This is the container for history

        // Existing spinner setup
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
        settings.setOnClickListener {
            val intent = Intent(this, SettingsInfoActivity::class.java)
            startActivity(intent)
        }


        // Display the history of selected colors
        displayColorHistory(historyContainer)
    }

    private fun openColorPickerDialog() {
        val colorPickerDialog = AmbilWarnaDialog(this, selectedColor, object : AmbilWarnaDialog.OnAmbilWarnaListener {
            override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                selectedColor = color
                val hexColor = String.format("#%06X", (0xFFFFFF and color))
                setColor(hexColor)
                saveColorToHistory(hexColor)
            }

            override fun onCancel(dialog: AmbilWarnaDialog?) {}
        })
        colorPickerDialog.show()
    }

    private fun setColor(color: String){
        findViewById<EditText>(R.id.colorInput).setText(color)
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

    private fun saveColorToHistory(color: String) {
        // Add color to history, remove the oldest if there are more than 5
        if (colorHistory.size >= 5) {
            colorHistory.removeAt(0)
        }
        colorHistory.add(color)
        displayColorHistory(findViewById(R.id.historyContainer))
    }

    private fun displayColorHistory(container: LinearLayout) {
        container.removeAllViews()

        val radius = 16f  // Adjust the radius as needed
        val size = 100  // Adjust the size of the square as needed

        // Display the colors from the history
        for (color in colorHistory) {
            val colorView = View(this)

            // Create a rounded square shape
            val shapeDrawable = ShapeDrawable()
            val shape = RoundRectShape(
                floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius),
                null, null
            )
            shapeDrawable.shape = shape
            shapeDrawable.paint.color = Color.parseColor(color)

            // Set the drawable as the background of the view
            colorView.background = shapeDrawable

            // Set size and margins for the color views
            val layoutParams = LinearLayout.LayoutParams(size, size)  // Square size
            layoutParams.setMargins(8, 8, 8, 8)
            colorView.layoutParams = layoutParams

            // Set onClickListener to update the colorInput
            colorView.setOnClickListener {
                setColor(color)
            }

            container.addView(colorView)
        }
    }
}
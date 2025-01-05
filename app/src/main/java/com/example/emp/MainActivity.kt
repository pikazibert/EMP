package com.example.emp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import yuku.ambilwarna.AmbilWarnaDialog
import android.text.TextWatcher
import android.text.Editable
import android.util.Log
import android.view.Gravity
import androidx.appcompat.app.AppCompatDelegate


class MainActivity : AppCompatActivity() {
    private var selectedColor: Int = Color.BLACK
    private val sharedPreferences by lazy { getSharedPreferences("Palettes", MODE_PRIVATE) }
    private val colorHistory = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        applySavedTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val colorInput = findViewById<EditText>(R.id.colorInput)
        val schemeSpinner = findViewById<Spinner>(R.id.schemeSpinner)
        val paletteContainer = findViewById<LinearLayout>(R.id.paletteContainer)
        val historyContainer = findViewById<LinearLayout>(R.id.historyContainer)
        val colorPickerButton = findViewById<ImageButton>(R.id.colorPickerButton)
        val savePaletteButton = findViewById<Button>(R.id.savePaletteButton)
        val viewSavedPalettesButton = findViewById<ImageButton>(R.id.viewSavedPalettesButton)
        val settings = findViewById<ImageButton>(R.id.settingsButton)

        val schemes = listOf("Complementary", "Monochromatic", "Analogous", "Triadic", "Tetradic")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, schemes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        schemeSpinner.adapter = adapter

        val defaultColorHex = "#${Integer.toHexString(selectedColor).substring(2).uppercase()}"
        colorInput.setText(defaultColorHex)

        updatePalette(defaultColorHex, schemeSpinner.selectedItem?.toString() ?: "Complementary")

        colorInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val hexColor = s.toString().trim()
                if (hexColor.matches(Regex("^#([A-Fa-f0-9]{6})$"))) {
                    updatePalette(hexColor, schemeSpinner.selectedItem?.toString() ?: "Complementary")
                }
            }
        })

        schemeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val hexColor = colorInput.text.toString().trim()
                if (hexColor.matches(Regex("^#([A-Fa-f0-9]{6})$"))) {
                    updatePalette(hexColor, parent.getItemAtPosition(position).toString())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        colorPickerButton.setOnClickListener {
            openColorPickerDialog { selectedColorHex ->
                colorInput.setText(selectedColorHex)
            }
        }

        savePaletteButton.setOnClickListener {
            for (i in 0 until paletteContainer.childCount) {
                val view = paletteContainer.getChildAt(i)
            }

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

    private fun openColorPickerDialog(onColorPicked: (String) -> Unit) {
        val colorPickerDialog = AmbilWarnaDialog(this, selectedColor, object : AmbilWarnaDialog.OnAmbilWarnaListener {
            override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                selectedColor = color
                val hexColor = String.format("#%06X", (0xFFFFFF and color))
                onColorPicked(hexColor)
                saveColorToHistory(hexColor)
            }

            override fun onCancel(dialog: AmbilWarnaDialog?) {}
        })
        colorPickerDialog.show()
    }

    private fun updatePalette(hexColor: String, schemeType: String) {
        val palette = when (schemeType) {
            "Complementary" -> ColorUtils.generateComplementaryColor(hexColor)
            "Monochromatic" -> ColorUtils.generateMonochromaticColors(hexColor)
            "Analogous" -> ColorUtils.generateAnalogousColors(hexColor)
            "Triadic" -> ColorUtils.generateTriadicColors(hexColor)
            "Tetradic" -> ColorUtils.generateTetradicColors(hexColor)
            else -> listOf(hexColor)
        }

        displayPalette(findViewById(R.id.paletteContainer), palette)
    }

    private fun displayPalette(container: LinearLayout, colors: List<String>) {
        container.removeAllViews()
        for (color in colors) {
            val colorView = LinearLayout(this).apply {
                val backgroundDrawable = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius = 16f
                    setColor(Color.parseColor(color))
                }
                background = backgroundDrawable
                layoutParams = LinearLayout.LayoutParams(0, 150, 1f).apply {
                    setMargins(8, 8, 8, 8)
                }
                tag = color
            }

            val colorCodeTextView = TextView(this).apply {
                text = color
                setTextColor(Color.BLACK)
                textSize = 12f
                gravity = Gravity.CENTER // Center-align the text horizontally and vertically
                visibility = TextView.GONE
            }

            colorCodeTextView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )

            colorView.addView(colorCodeTextView)

            colorView.setOnClickListener {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Color Code", color)
                clipboard.setPrimaryClip(clip)

                // Show the HEX code temporarily
                colorCodeTextView.visibility = TextView.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    colorCodeTextView.visibility = TextView.GONE
                }, 5000) // Show the text for 5 seconds
            }

            container.addView(colorView)
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
        if (colorHistory.size >= 5) {
            colorHistory.removeAt(0)
        }
        colorHistory.add(color)
        displayColorHistory(findViewById(R.id.historyContainer))
    }

    private fun displayColorHistory(container: LinearLayout) {
        container.removeAllViews()
        val radius = 16f
        val size = 100
        for (color in colorHistory) {
            val colorView = LinearLayout(this).apply {
                val backgroundDrawable = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius = radius
                    setColor(Color.parseColor(color))
                }
                background = backgroundDrawable
                layoutParams = LinearLayout.LayoutParams(size, size).apply {
                    setMargins(8, 8, 8, 8)
                }
            }

            val colorCodeTextView = TextView(this).apply {
                text = color
                setTextColor(Color.BLACK)
                textSize = 10f
                gravity = Gravity.CENTER
                visibility = TextView.GONE
            }

            colorCodeTextView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )

            colorView.addView(colorCodeTextView)

            colorView.setOnClickListener {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Color Code", color)
                clipboard.setPrimaryClip(clip)

                // Show the HEX code temporarily
                colorCodeTextView.visibility = TextView.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    colorCodeTextView.visibility = TextView.GONE
                }, 5000) // Show the text for 5 seconds
            }

            container.addView(colorView)
        }
    }

    private fun applySavedTheme() {
        val savedTheme = sharedPreferences.getString("theme", "Auto")
        when (savedTheme) {
            "Auto" ->  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            "Night" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "Day" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

}

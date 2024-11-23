package com.example.emp

import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SavedPalettesActivity : AppCompatActivity() {

    private val sharedPreferences by lazy { getSharedPreferences("Palettes", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_palettes)

        val paletteContainer = findViewById<LinearLayout>(R.id.savedPaletteContainer)

        val savedPalettes = loadAllPalettes()

        if (savedPalettes.isEmpty()) {
            Toast.makeText(this, "No saved palettes found!", Toast.LENGTH_SHORT).show()
        } else {
            displayPalettes(paletteContainer, savedPalettes)
        }
    }

    private fun loadAllPalettes(): List<List<String>> {
        return sharedPreferences.all.values.mapNotNull { value ->
            if (value is Set<*>) {
                value.mapNotNull { it.toString() }
            } else {
                null
            }
        }
    }

    private fun displayPalettes(container: LinearLayout, palettes: List<List<String>>) {
        container.removeAllViews()
        for (palette in palettes) {
            val paletteRow = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setPadding(0, 16, 0, 16)
            }

            for (color in palette) {
                val colorView = createColorView(color)
                paletteRow.addView(colorView)
            }

            container.addView(paletteRow)
        }
    }

    private fun createColorView(color: String): LinearLayout {
        val colorView = LinearLayout(this)
        colorView.setBackgroundColor(Color.parseColor(color))
        val layoutParams = LinearLayout.LayoutParams(0, 150, 1f)
        layoutParams.setMargins(8, 8, 8, 8)
        colorView.layoutParams = layoutParams
        return colorView
    }
}

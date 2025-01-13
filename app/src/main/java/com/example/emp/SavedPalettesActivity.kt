package com.example.emp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SavedPalettesActivity : AppCompatActivity() {

    private val sharedPreferences by lazy { getSharedPreferences("Palettes", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_palettes)

        Log.d("SavedPalettesActivityLog", "onCreate called")

        val paletteContainer = findViewById<LinearLayout>(R.id.savedPaletteContainer)
        val spinner = findViewById<Spinner>(R.id.filterDropdown)
        val savedPalettes = loadAllPalettes()

        if (savedPalettes.isEmpty()) {
            Toast.makeText(this, "No saved palettes found!", Toast.LENGTH_SHORT).show()
        } else {
            displayPalettes(paletteContainer, savedPalettes)
        }

        val filters = listOf("All", "Liked", "2 Colors", "3 Colors", "4 Colors")
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            filters
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedFilter = filters[position]
                val filteredPalettes = when (selectedFilter) {
                    "Liked" -> savedPalettes.filter { isLiked(it.second) }
                    "2 Colors" -> savedPalettes.filter { it.first.size == 2 }
                    "3 Colors" -> savedPalettes.filter { it.first.size == 3 }
                    "4 Colors" -> savedPalettes.filter { it.first.size == 4 }
                    else -> savedPalettes
                }
                displayPalettes(paletteContainer, filteredPalettes)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action needed
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("SavedPalettesActivityLog", "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("SavedPalettesActivityLog", "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("SavedPalettesActivityLog", "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("SavedPalettesActivityLog", "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("SavedPalettesActivityLog", "onDestroy called")
    }

    private fun loadAllPalettes(): List<Pair<List<String>, String>> {
        val palettesWithLikes = mutableListOf<Pair<List<String>, String>>()
        sharedPreferences.all.forEach { (key, value) ->
            if (value is Set<*>) {
                val palette = value.mapNotNull { it.toString() }
                palettesWithLikes.add(Pair(palette, key))
            }
        }
        return palettesWithLikes
    }

    private fun displayPalettes(
        container: LinearLayout,
        palettes: List<Pair<List<String>, String>>
    ) {
        container.removeAllViews()
        for ((palette, key) in palettes) {
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
            val likeButton = ImageView(this).apply {
                setImageResource(if (isLiked(key)) R.drawable.like else R.drawable.no_like)
                layoutParams = LinearLayout.LayoutParams(60, 60).apply {
                    setMargins(0, 10, 0, 0)
                }
                setOnClickListener {
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("liked_$key", !isLiked(key))
                    editor.apply()
                    setImageResource(if (isLiked(key)) R.drawable.like else R.drawable.no_like)
                }
            }
            val shareButton = ImageView(this).apply {
                setImageResource(R.drawable.share)
                layoutParams = LinearLayout.LayoutParams(60, 60).apply {
                    setMargins(0, 0, 0, 10)
                }
                setOnClickListener {
                    sharePalette(palette)
                }
            }
            val space = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0
                ).apply {
                    weight = 1f
                }
            }
            val buttonContainer = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                ).apply {
                    setMargins(16, 0, 16, 0)
                }
            }
            buttonContainer.addView(likeButton)
            buttonContainer.addView(space)
            buttonContainer.addView(shareButton)
            paletteRow.addView(buttonContainer)
            container.addView(paletteRow)
        }
    }

    private fun createColorView(color: String): LinearLayout {
        val colorView = LinearLayout(this)
        val backgroundDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 16f
            setColor(Color.parseColor(color))
        }
        colorView.background = backgroundDrawable
        val layoutParams = LinearLayout.LayoutParams(0, 150, 1f)
        layoutParams.setMargins(8, 8, 8, 8)
        colorView.layoutParams = layoutParams

        val colorCodeTextView = TextView(this).apply {
            text = color
            setTextColor(Color.BLACK)
            textSize = 12f
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
            colorCodeTextView.visibility = TextView.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                colorCodeTextView.visibility = TextView.GONE
            }, 5000)
        }

        return colorView
    }

    private fun sharePalette(palette: List<String>) {
        val paletteColors = palette.joinToString(", ") { it }
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Check out this palette: $paletteColors")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    private fun isLiked(key: String): Boolean {
        return sharedPreferences.getBoolean("liked_$key", false)
    }
}

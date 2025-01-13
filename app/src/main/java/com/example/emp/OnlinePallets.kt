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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException

class OnlinePallets : AppCompatActivity() {
    private val sharedPreferences by lazy { getSharedPreferences("Palettes", MODE_PRIVATE) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_pallets)

        val spinner = findViewById<Spinner>(R.id.filterDropdown)

        val filters = listOf("Popular", "New", "Random")
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
                val selectedFilter = filters[position].lowercase()
                showPublicPalettes(selectedFilter)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action needed
            }
        }
    }

    private fun fetchPalettesFromAPI(category: String, onResult: (List<Pair<List<String>, String>>) -> Unit) {
        val url = when (category) {
            "popular" -> "https://www.colourlovers.com/api/palettes/top?format=json&numResults=10"
            "new" -> "https://www.colourlovers.com/api/palettes/new?format=json&numResults=10"
            "random" -> "https://www.colourlovers.com/api/palettes/random?format=json&numResults=10"
            else -> throw IllegalArgumentException("Invalid category")
        }

        val request = StringRequest(Request.Method.GET, url, { response ->
            Log.d("API_RESPONSE", response) // Dodano za prikaz odgovora
            try {
                val jsonArray = JSONArray(response)
                val palettes = mutableListOf<Pair<List<String>, String>>()
                for (i in 0 until jsonArray.length()) {
                    val paletteObj = jsonArray.getJSONObject(i)
                    val colorsArray = paletteObj.getJSONArray("colors")
                    val colors = mutableListOf<String>()
                    for (j in 0 until colorsArray.length()) {
                        colors.add("#" + colorsArray.getString(j))
                    }
                    val title = paletteObj.getString("title")
                    palettes.add(Pair(colors, title))
                }
                onResult(palettes)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, { error ->
            Log.e("API_ERROR", "Error: ${error.message}") // Dodano za prikaz napake
            error.printStackTrace()
        })

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(request)
    }

    private fun showPublicPalettes(category: String) {
        val container = findViewById<LinearLayout>(R.id.paletteContainer)
        fetchPalettesFromAPI(category) { palettes ->
            displayPalettes(container, palettes)
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
            colorCodeTextView.visibility = TextView.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                colorCodeTextView.visibility = TextView.GONE
            }, 5000)
        }

        return colorView
    }
    private fun savePaletteToPreferences(palette: List<String>, isDownloaded: Boolean = false) {
        val editor = sharedPreferences.edit()
        val key = "palette_${System.currentTimeMillis()}"
        editor.putStringSet(key, palette.toSet())
        editor.putBoolean("liked_$key", false)
        editor.putBoolean("downloaded_$key", isDownloaded)
        editor.apply()
    }

    private fun displayPalettes(
        container: LinearLayout,
        palettes: List<Pair<List<String>, String>>
    ) {
        Log.d("DISPLAY_PALETTES", "Palettes count: ${palettes.size}") // Dodano za prikaz števila palet
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
            val saveButton = ImageView(this).apply {
                if (isInShared(palette)) {
                    // Ko je paleta že shranjena, prikaži no_save ikono
                    setImageResource(R.drawable.no_save)
                    layoutParams = LinearLayout.LayoutParams(60, 60).apply {
                        setMargins(0, 0, 0, 10)
                    }

                    setOnClickListener {
                        // Pokliči funkcijo unsavePalette, ko je paleta že shranjena
                        unsavePalette(palette)
                        // Posodobi sliko, ker je bila paleta odstranjena
                        setImageResource(R.drawable.save)  // Prikazuj sliko za "shranjevanje"
                        Toast.makeText(applicationContext, "Palette removed!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Ko paleta še ni shranjena, prikaži save ikono
                    setImageResource(R.drawable.save)
                    layoutParams = LinearLayout.LayoutParams(60, 60).apply {
                        setMargins(0, 0, 0, 10)
                    }

                    setOnClickListener {
                        if (palette.isNotEmpty()) {
                            // Shrani paleto, ko še ni shranjena
                            savePaletteToPreferences(palette, isDownloaded = true)
                            // Posodobi sliko, ker je bila paleta shranjena
                            setImageResource(R.drawable.no_save)  // Prikazuj sliko za "odstranitev"
                            Toast.makeText(applicationContext, "Palette saved!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "No palette to save!", Toast.LENGTH_SHORT).show()
                        }
                    }
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
            buttonContainer.addView(saveButton)
            buttonContainer.addView(space)
            buttonContainer.addView(shareButton)
            paletteRow.addView(buttonContainer)
            container.addView(paletteRow)
        }
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
    private fun unsavePalette(palette: List<String>) {
        val editor = sharedPreferences.edit()
        val storedPalettes = sharedPreferences.all

        for (entry in storedPalettes) {
            val savedPalette = entry.value as? Set<String>
            if (savedPalette != null && savedPalette == palette.toSet()) {
                editor.remove(entry.key)  // Odstrani to paleto po ključu
                editor.apply()
                Toast.makeText(applicationContext, "Palette removed!", Toast.LENGTH_SHORT).show()
                break
            }
        }
    }
    private fun isInShared(palette: List<String>): Boolean {
        val storedPalettes = sharedPreferences.all
        for (entry in storedPalettes) {
            // entry.key je lahko "palette_xxxxxxxx" in entry.value je Set<String>
            val savedPalette = entry.value as? Set<String>
            if (savedPalette != null && savedPalette == palette.toSet()) {
                return true
            }
        }
        return false
    }
}

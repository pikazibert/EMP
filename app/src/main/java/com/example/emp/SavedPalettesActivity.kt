package com.example.emp

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SavedPalettesActivity : AppCompatActivity() {

    // Ustvarimo lazy inicializirano referenco na SharedPreferences, kjer so shranjene barvne palete.
    private val sharedPreferences by lazy { getSharedPreferences("Palettes", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_palettes)

        // Poiščemo LinearLayout iz XML datoteke za prikazovanje shranjenih palet.
        val paletteContainer = findViewById<LinearLayout>(R.id.savedPaletteContainer)

        // Naložimo vse shranjene barvne palete.
        val savedPalettes = loadAllPalettes()

        // Če ni shranjenih palet, prikažemo Toast sporočilo.
        if (savedPalettes.isEmpty()) {
            Toast.makeText(this, "No saved palettes found!", Toast.LENGTH_SHORT).show()
        } else {
            // Če so palete prisotne, jih prikažemo.
            displayPalettes(paletteContainer, savedPalettes)
        }
    }

    /**
     * Naloži vse shranjene barvne palete iz SharedPreferences.
     * Vrne seznam palet, kjer je vsaka paleta predstavljena kot seznam nizov z barvnimi kodami.

*/
    private fun loadAllPalettes(): List<Pair<List<String>, String>> {
        val palettesWithLikes = mutableListOf<Pair<List<String>, String>>()

        // Preberi vse ključe iz SharedPreferences (seznam vseh palet).
        sharedPreferences.all.forEach { (key, value) ->
            // Preveri, če je vrednost tipa Set (to je barvna paleta).
            if (value is Set<*>) {
                // Pretvori Set v seznam nizov.
                val palette = value.mapNotNull { it.toString() }

                // Preveri, ali je ta paleta "liked" (oziroma njen status).
                //val isLiked = sharedPreferences.getBoolean("liked_$key", false)

                // Dodaj paleto in njen "liked" status v seznam.
                palettesWithLikes.add(Pair(palette, key))
            }
        }

        return palettesWithLikes
    }

    /**
     * Prikaže vse shranjene palete v podanem LinearLayout.
     * Vsaka paleta se prikaže kot vrstica z barvnimi pogledi.
     */
    private fun displayPalettes(container: LinearLayout, palettes: List<Pair<List<String>, String>>) {
        container.removeAllViews() // Počistimo obstoječe poglede.

        for ((palette, key) in palettes) {
            // Glavni kontejner za eno paleto
            val paletteRow = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL // Horizontalna postavitev
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setPadding(0, 16, 0, 16) // Razmiki med vrsticami
            }

            // Dodamo barvne kvadrate
            for (color in palette) {
                val colorView = createColorView(color)
                paletteRow.addView(colorView)
            }

            // Dodamo gumba za "Like" in "Share"
            // Gumb "Like" z ikono (thumb-up)
            val likeButton = ImageView(this).apply {
                if(DarkOn()) {
                    setImageResource(if (isLiked(key)) R.drawable.like else R.drawable.no_like_dark) // Nastavimo ikono glede na to, ali je bila paleta všeč
                }else{
                    setImageResource(if (isLiked(key)) R.drawable.like else R.drawable.no_like) // Nastavimo ikono glede na to, ali je bila paleta všeč
                }
                layoutParams = LinearLayout.LayoutParams(60, 60).apply {
                    setMargins(0, 10, 0, 0) // Razmiki okoli gumba
                } // Velikost ikone
                setOnClickListener {
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("liked_$key", !isLiked(key)) // Posodobi "liked" status za ta ključ
                    editor.apply()
                    if(DarkOn()) {
                        setImageResource(if (isLiked(key)) R.drawable.like else R.drawable.no_like_dark) // Nastavimo ikono glede na to, ali je bila paleta všeč
                    }else{
                        setImageResource(if (isLiked(key)) R.drawable.like else R.drawable.no_like) // Nastavimo ikono glede na to, ali je bila paleta všeč
                    }
                }
            }

            val shareButton = ImageView(this).apply {
                if(DarkOn()) {
                    setImageResource(R.drawable.share_dark) // Nastavite vir slike (share)
                }else{
                    setImageResource(R.drawable.share) // Nastavite vir slike (share)

                }
                layoutParams = LinearLayout.LayoutParams(60, 60).apply {
                    setMargins(0, 0, 0, 10) // Razmiki okoli gumba
                } // Velikost ikone
                setOnClickListener {
                    sharePalette(palette) // Funkcija za deljenje palete
                }
            }

            // Dodajte vertikalni prostor med gumbi z uporabo Dummy View ali prazen LinearLayout
            val space = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, // Polna širina
                    0 // Brez dejanske višine
                ).apply {
                    weight = 1f // Dodelite težišče, da ustvari prostor med gumbi
                }
            }

            // Ustvarimo vertikalni kontejner za gumba
            val buttonContainer = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL // Vertikalna postavitev
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                ).apply {
                    setMargins(16, 0, 16, 0) // Razmiki okoli gumba
                }
            }

// Dodamo gumba v vertikalni kontejner
            buttonContainer.addView(likeButton)
            buttonContainer.addView(space) // Prazni prostor med gumboma
            buttonContainer.addView(shareButton)

// Dodamo kontejner z gumbi v vrstico
            paletteRow.addView(buttonContainer)
            // Dodamo vrstico v glavni vsebnik
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
    /**
     * Ustvari in vrne LinearLayout, ki prikazuje posamezno barvo.
     * Barva je določena z nizom, ki predstavlja barvno kodo (npr. "#FF0000").



    stara funkcija, hocem zaobljene robove
    private fun createColorView(color: String): LinearLayout {
        val colorView = LinearLayout(this)
        colorView.setBackgroundColor(Color.parseColor(color)) // Nastavimo ozadje na določeno barvo.
        val layoutParams = LinearLayout.LayoutParams(0, 150, 1f) // Širina proporcionalna (1f) za enakomerno porazdelitev.
        layoutParams.setMargins(8, 8, 8, 8) // Določimo robove za barvni pogled.
        colorView.layoutParams = layoutParams
        return colorView
    }
    */

    private fun createColorView(color: String): LinearLayout {
        val colorView = LinearLayout(this)

        // Ustvarimo GradientDrawable za zaobljene robove.
        val backgroundDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 16f // Polmer za zaobljene robove (v dp, 16f je približno 8dp).
            setColor(Color.parseColor(color)) // Nastavimo barvo ozadja.
        }

        colorView.background = backgroundDrawable // Nastavimo ozadje pogleda.

        // Nastavimo parametre za postavitev in robove.
        val layoutParams = LinearLayout.LayoutParams(0, 150, 1f)
        layoutParams.setMargins(8, 8, 8, 8)
        colorView.layoutParams = layoutParams

        return colorView
    }

    private fun isLiked(key: String): Boolean {
        return sharedPreferences.getBoolean("liked_$key", false)
    }


    /*
    nam pove ali je omogocen darkmode
     */
    private fun DarkOn(): Boolean {
        val nightModeFlags = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == android.content.res.Configuration.UI_MODE_NIGHT_YES
    }
}
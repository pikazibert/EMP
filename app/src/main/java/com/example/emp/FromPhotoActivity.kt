package com.example.emp

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException

class FromPhotoActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private val TAKE_PHOTO_REQUEST = 2
    private lateinit var imageView: ImageView
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_from_photo)

        imageView = findViewById(R.id.imageView)
        val selectPhotoButton: Button = findViewById(R.id.selectPhotoButton)
        val takePhotoButton: Button = findViewById(R.id.takePhotoButton)

        selectPhotoButton.setOnClickListener { openGallery() }
        takePhotoButton.setOnClickListener { takePhoto() }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, TAKE_PHOTO_REQUEST)
    }
    val maxHeight = 600  // Maximum height in pixels

    fun setImage(bitmap: Bitmap) {
        if (bitmap.height > maxHeight) {
            // Scale the image to fit within the maxHeight while maintaining aspect ratio
            val scaleFactor = maxHeight.toFloat() / bitmap.height
            val scaledBitmap = Bitmap.createScaledBitmap(
                bitmap,
                (bitmap.width * scaleFactor).toInt(),
                maxHeight,
                true
            )
            imageView.setImageBitmap(scaledBitmap)
        } else {
            imageView.setImageBitmap(bitmap)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val placeholderText: TextView = findViewById(R.id.placeholderText) // Poiščite placeholder

        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    val uri = data.data
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    setImage(bitmap)
                    placeholderText.visibility = View.GONE // Skrij placeholder
                    sendPhotoToApi(bitmap)
                }
                TAKE_PHOTO_REQUEST -> {
                    val bitmap = data.extras?.get("data") as Bitmap
                    setImage(bitmap)
                    placeholderText.visibility = View.GONE // Skrij placeholder
                    sendPhotoToApi(bitmap)
                }
            }
        }
    }


    private fun sendPhotoToApi(bitmap: Bitmap) {
        val url = "http://1kp3.com:5000/analyze"
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                "photo.jpg",
                RequestBody.create("image/jpeg".toMediaTypeOrNull(), byteArray)
            )
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("FromPhotoActivity", "API request failed: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    Log.d("FromPhotoActivity", "API response: $responseBody")

                    // Razčleni JSON in pridobi barvne kode
                    val json = JSONObject(responseBody ?: "")
                    val colors = mutableListOf<String>()
                    json.keys().forEach { key ->
                        colors.add(key) // Dodaj HEX barvne kode
                    }

                    // Ustvari kvadrate in jih prikaži na zaslonu
                    runOnUiThread {
                        val gridLayout = findViewById<GridLayout>(R.id.colorGridLayout)
                        gridLayout.removeAllViews() // Počisti stare kvadrate, če obstajajo

                        for (color in colors) {
                            val colorView = View(this@FromPhotoActivity)

                            // Ustvari GradientDrawable z zaobljenimi robovi
                            val roundedDrawable = GradientDrawable().apply {
                                shape = GradientDrawable.RECTANGLE
                                cornerRadius = 16f // Nastavi zaobljenost robov (prilagodi po potrebi)
                                setColor(Color.parseColor(color)) // Nastavi barvo ozadja
                            }

                            // Nastavi ozadje za barvni kvadrat
                            colorView.background = roundedDrawable

                            // Nastavi layout params za GridLayout
                            colorView.layoutParams = GridLayout.LayoutParams().apply {
                                width = 0
                                height = 100
                                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                                setMargins(8, 8, 8, 8)
                            }

                            val colorCodeTextView = TextView(this@FromPhotoActivity).apply {
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


                            colorView.setOnClickListener {
                                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Color Code", color)
                                clipboard.setPrimaryClip(clip)
                                colorCodeTextView.visibility = TextView.VISIBLE
                                Handler(Looper.getMainLooper()).postDelayed({
                                    colorCodeTextView.visibility = TextView.GONE
                                }, 5000)
                            }

                            // Dodaj View v GridLayout
                            gridLayout.addView(colorView)
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            this@FromPhotoActivity,
                            "Error: ${response.code}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    Log.e("FromPhotoActivity", "API request failed with status: ${response.code}")
                }
            }
        })
    }
}
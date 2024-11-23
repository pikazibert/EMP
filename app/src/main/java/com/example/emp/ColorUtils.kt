package com.example.emp

import android.graphics.Color

object ColorUtils {

    fun generateComplementaryColor(hex: String): List<String> {
        val color = Color.parseColor(hex)
        val complementary = Color.rgb(255 - Color.red(color), 255 - Color.green(color), 255 - Color.blue(color))
        return listOf(hex, String.format("#%06X", 0xFFFFFF and complementary))
    }

    fun generateMonochromaticColors(hex: String): List<String> {
        val baseColor = Color.parseColor(hex)
        return listOf(
            adjustBrightness(baseColor, 0.8f),
            hex,
            adjustBrightness(baseColor, 1.2f)
        )
    }

    fun generateAnalogousColors(hex: String): List<String> {
        val baseColor = Color.parseColor(hex)
        return listOf(
            adjustHue(baseColor, -30f),
            hex,
            adjustHue(baseColor, 30f)
        )
    }

    fun generateTriadicColors(hex: String): List<String> {
        val baseColor = Color.parseColor(hex)
        return listOf(
            adjustHue(baseColor, 120f),
            hex,
            adjustHue(baseColor, 240f)
        )
    }

    fun generateTetradicColors(hex: String): List<String> {
        val baseColor = Color.parseColor(hex)
        return listOf(
            adjustHue(baseColor, 90f),
            hex,
            adjustHue(baseColor, 180f),
            adjustHue(baseColor, 270f)
        )
    }

    private fun adjustBrightness(color: Int, factor: Float): String {
        val r = (Color.red(color) * factor).coerceIn(0f, 255f).toInt()
        val g = (Color.green(color) * factor).coerceIn(0f, 255f).toInt()
        val b = (Color.blue(color) * factor).coerceIn(0f, 255f).toInt()
        return String.format("#%06X", 0xFFFFFF and Color.rgb(r, g, b))
    }

    private fun adjustHue(color: Int, degrees: Float): String {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[0] = (hsv[0] + degrees + 360) % 360
        return String.format("#%06X", 0xFFFFFF and Color.HSVToColor(hsv))
    }
}

package ca.jonathanfritz.ktgame.engine.colour

import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NanoVG

data class RGBColour(val red: Int, val green: Int, val blue: Int) {
    val nvgColor: NVGColor = NanoVG.nvgRGB(
        red.coerceIn(0, 255).toByte(),
        green.coerceIn(0, 255).toByte(),
        blue.coerceIn(0, 255).toByte(), NVGColor.create()
    )
}
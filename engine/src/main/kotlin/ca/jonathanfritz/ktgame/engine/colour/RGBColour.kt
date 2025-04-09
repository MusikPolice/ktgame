package ca.jonathanfritz.ktgame.engine.colour

import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NanoVG

class RGBColour(red: Int, green: Int, blue: Int) {

    // an rgb colour can be packed into three bytes
    private val bytes: ByteArray = ByteArray(3)

    init {
        bytes[0] = (red.coerceIn(0, 255) and 0xFF).toByte()
        bytes[1] = (green.coerceIn(0, 255) and 0xFF).toByte()
        bytes[2] = (blue.coerceIn(0, 255) and 0xFF).toByte()
    }

    val red = bytes[0].toInt()
    val green = bytes[1].toInt()
    val blue = bytes[2].toInt()

    fun toNVGColor(): NVGColor = NanoVG.nvgRGB(bytes[0], bytes[1], bytes[2], NVGColor.create())
}
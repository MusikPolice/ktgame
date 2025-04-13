package ca.jonathanfritz.ktgame.engine.utils

import ca.jonathanfritz.ktgame.engine.NVG
import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NanoVG

class FPSCounter(
    private val fontName: String,
) {
    private var lastTime: Long = System.nanoTime()
    private var frameCount: Int = 0
    private var fps: Int = 0

    fun update() {
        val currentTime = System.nanoTime()
        frameCount++

        if (currentTime - lastTime >= 1_000_000_000) {
            fps = frameCount
            frameCount = 0
            lastTime = currentTime
        }
    }

    /**
     * Draws an fps counter in the top-left corner of the viewport
     */
    fun render(nvg: NVG) {
        NanoVG.nvgBeginPath(nvg)
        NanoVG.nvgFontSize(nvg, 20f)
        NanoVG.nvgFontFace(nvg, fontName)
        NanoVG.nvgFillColor(nvg, NanoVG.nvgRGB(255.toByte(), 255.toByte(), 255.toByte(), NVGColor.create()))
        NanoVG.nvgText(nvg, 10f, 20f, "FPS: $fps")
    }
}

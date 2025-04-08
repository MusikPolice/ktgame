package ca.jonathanfritz.ktgame.engine

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.nanovg.NanoVGGL3
import org.lwjgl.opengl.GL

// the GLFW and NanoVG libraries use Longs to represent their respective context handles
typealias NVG = Long
typealias Window = Long

data class Viewport (val scene: Scene, val title: String, val width: Int, val height: Int): AutoCloseable {

    private val window: Window
    private val nvg: NVG

    init {
        // initialize GLFW
        GLFWErrorCallback.createPrint(System.err).set()
        if (!GLFW.glfwInit()) {
            throw IllegalStateException("Unable to initialize GLFW")
        }
        GLFW.glfwDefaultWindowHints()
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE)
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE)

        // create a window to render to - this returns a window handle as a long
        window = GLFW.glfwCreateWindow(width, height, title, 0L, 0L)
        if (window == 0L) {
            throw RuntimeException("Failed to create the GLFW window")
        }

        // make the OpenGL context current
        GLFW.glfwMakeContextCurrent(window)

        // enable v-sync
        GLFW.glfwSwapInterval(1)

        // make the window visible
        GLFW.glfwShowWindow(window)

        // init opengl
        GL.createCapabilities()

        // init nanovg
        nvg = NanoVGGL3.nvgCreate(NanoVGGL3.NVG_ANTIALIAS or NanoVGGL3.NVG_STENCIL_STROKES)
        if (nvg == 0L) {
            throw RuntimeException("Failed to create NanoVG context")
        }

        // load any resources needed for the scene
        scene.loadResources(nvg)

        // start the main loop
        mainLoop()
    }

    private fun mainLoop() {
        var lastTickNanos = System.nanoTime()

        // loop until the window is closed
        while (!GLFW.glfwWindowShouldClose(window)) {

            // draw the scene
            scene.render(nvg)

            // update the scene
            val now = System.nanoTime()
            scene.update(now - lastTickNanos)
            lastTickNanos = now

            // TODO: sleep to cap frame rate?
        }
    }

    override fun close() {
        // cleanup scene resources
        scene.unloadResources()

        // cleanup nanovg
        NanoVGGL3.nvgDelete(nvg)

        // cleanup GLFW
        GLFW.glfwTerminate()
        GLFW.glfwSetErrorCallback(null)?.free()
    }
}
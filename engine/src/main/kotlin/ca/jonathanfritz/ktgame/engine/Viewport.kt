package ca.jonathanfritz.ktgame.engine

import io.github.oshai.kotlinlogging.KotlinLogging
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.nanovg.NanoVG
import org.lwjgl.nanovg.NanoVGGL3
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*

// the GLFW and NanoVG libraries use Longs to represent their respective context handles
typealias NVG = Long
typealias Window = Long

private val log = KotlinLogging.logger {}

data class Viewport (val scene: Scene, val title: String, val width: Int, val height: Int): AutoCloseable {

    private val window: Window
    private val nvg: NVG

    init {
        log.debug { "Initializing Viewport..." }

        // initialize GLFW
        GLFWErrorCallback.createPrint(System.err).set()
        if (!GLFW.glfwInit()) {
            throw IllegalStateException("Unable to initialize GLFW")
        }
        GLFW.glfwDefaultWindowHints()
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE)
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE)
        log.debug { "Initialized GLFW" }

        // create a window to render to - this returns a window handle as a long
        window = GLFW.glfwCreateWindow(width, height, title, 0L, 0L)
        if (window == 0L) {
            throw RuntimeException("Failed to create the GLFW window")
        }

        GLFW.glfwMakeContextCurrent(window)
        log.debug { "Created GLFW window $window" }

        GLFW.glfwSwapInterval(1)
        log.debug { "Enabled v-sync" }

        GLFW.glfwShowWindow(window)
        log.debug { "Made the GLFW window visible" }

        // init opengl
        GL.createCapabilities()
        log.debug { "Initialized OpenGL" }

        // init nanovg
        nvg = NanoVGGL3.nvgCreate(NanoVGGL3.NVG_ANTIALIAS or NanoVGGL3.NVG_STENCIL_STROKES)
        if (nvg == 0L) {
            throw RuntimeException("Failed to create NanoVG context")
        }
        log.debug { "Initialized NanoVG context $nvg" }

        log.debug { "Loading Scene Resources..." }
        // load any resources needed for the scene
        scene.loadResources(nvg)
        log.debug { "Loaded Scene Resources" }

        // start the main loop
        mainLoop()
    }

    private fun mainLoop() {
        var lastTickNanos = System.nanoTime()
        log.debug { "Started the main loop at $lastTickNanos" }

        // loop until the window is closed
        while (!GLFW.glfwWindowShouldClose(window)) {
            // clear the frame buffer and begin a new frame
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
            NanoVG.nvgBeginFrame(nvg, width.toFloat(), height.toFloat(), 1f)

            // draw the scene
            scene.render(nvg)

            // end the frame and swap the buffers
            NanoVG.nvgEndFrame(nvg)
            GLFW.glfwSwapBuffers(window)

            // poll for window events
            GLFW.glfwPollEvents()

            // update the scene
            val now = System.nanoTime()
            scene.update(now - lastTickNanos)
            lastTickNanos = now

            // TODO: sleep to cap frame rate?
        }
        log.debug { "Main loop stopped" }
    }

    override fun close() {
        // cleanup scene resources
        log.debug { "Unloading Scene Resources..." }
        scene.unloadResources()
        log.debug { "Unloaded Scene Resources" }

        // cleanup nanovg
        NanoVGGL3.nvgDelete(nvg)
        log.debug { "Deleted NanoVG context $nvg" }

        // cleanup GLFW
        GLFW.glfwTerminate()
        GLFW.glfwSetErrorCallback(null)?.free()
        log.debug { "Cleaned up GLFW" }
    }
}
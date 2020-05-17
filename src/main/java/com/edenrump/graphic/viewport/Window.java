package com.edenrump.graphic.viewport;

import com.edenrump.graphic.time.Time;
import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.awt.*;
import java.io.PrintStream;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Simple Window based on www.lwjgl.org/guide
 * <p>
 * Provides access to display initialisation, buffer swap and termination.
 * <p>
 */
public class Window {

    private long windowID;

    private Time gameTime = Time.getInstance();
    private double widthProportion, heighProportion;
    private String applicationName;
    private Color defaultBackground = Color.LIGHT_GRAY;

    /**
     * Creates a GLFW display with basic settings:
     * <p>
     * Error callbacks are defaulted to System.err
     * Window is resizeable, and will be invisible until explicitly shown
     * glfwCallback set to close window on esc (for ease)
     * new window is centered on primary scene
     *
     * @param proportionalW widthProportion of display
     * @param proportionalH heighProportion of display
     * @param color         default pixel colour for buffer
     * @param appTitle      header title for window
     */
    public Window(double proportionalW, double proportionalH, String appTitle, Color color) {
        // Setup an error callback. The default implementation will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();
        widthProportion = proportionalW;
        heighProportion = proportionalH;
        applicationName = appTitle;
        defaultBackground = color;
    }

    /* ****************************************************************************************************************
     * Timer functions
     * ****************************************************************************************************************/

    /**
     * Helper method to print the current version of LWJGL being used to create this display.
     *
     * @param printStream printStream that will be used to print the version
     */
    public static void printVersion(PrintStream printStream) {
        printStream.println("LWJGL " + Version.getVersion() + "!");
    }

    /**
     * Creates a new window using current widthProportion, heighProportion, title and background colour
     * Does not take any arguments
     */
    public void create(boolean fullscreen) {

        if (widthProportion == 0 || heighProportion == 0 || applicationName == null)
            throw new IllegalStateException("Window heighProportion, widthProportion or title has not been initialised");

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Get the resolution of the primary monitor
        GLFWVidMode primaryScreenResolution = glfwGetVideoMode(glfwGetPrimaryMonitor());

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will not be resizable

        int width = (int) Math.round(widthProportion * primaryScreenResolution.width());
        int height = (int) Math.round(heighProportion * primaryScreenResolution.height());

        // Create the window
        windowID = glfwCreateWindow(
                width,
                height,
                applicationName,
                fullscreen ? glfwGetPrimaryMonitor() : NULL, NULL);
        if (windowID == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(windowID, (windowID, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(windowID, true); // We will detect this in the rendering loop
        });

        glfwSetWindowSizeCallback(windowID, recalculateSize());

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(windowID, pWidth, pHeight);

            // Center the window
            glfwSetWindowPos(
                    windowID,
                    (primaryScreenResolution.width() - pWidth.get(0)) / 2,
                    (primaryScreenResolution.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowID);
        // Enable v-sync
        glfwSwapInterval(2);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        prepareForRender();
    }

    /**
     * Shows the window for this Window
     */
    public void show() {
        // Make the window visible
        glfwShowWindow(windowID);
    }

    /**
     * Closes the window for this Window and clears up callbacks.
     */
    public void terminate() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(windowID);
        glfwDestroyWindow(windowID);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    /* ****************************************************************************************************************
     * Static Methods - Helpers and Info
     * ****************************************************************************************************************/

    /**
     * Exposes glfwWindowShouldClose(windowID) to calling class
     *
     * @return glfwWindowShouldClose(windowID)
     */
    public boolean isCloseRequested() {
        return glfwWindowShouldClose(windowID);
    }

    /* ****************************************************************************************************************
     * Window Defaults
     * ****************************************************************************************************************/

    /**
     * Gets elapsed time since last display refresh
     *
     * @return difference between glfwGetTime() at last frame and current frame
     */
    public double getFrameTimeSeconds() {
        return gameTime.getDeltaTime();
    }

    public void update() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
        glfwPollEvents();// Poll for window events. The key callback above will only be invoked during this call.
        recalculateSize();
        updateWindowTitle();
    }

    public void prepareForRender() {
        glClear(GL_COLOR_BUFFER_BIT);
        glClearColor(
                defaultBackground.getRed(),
                defaultBackground.getGreen(),
                defaultBackground.getBlue(),
                0.0f);
    }

    /**
     * Clears the framebuffer and swaps back buffer to front
     * Stores the current frame time and updates delta to time since last frame
     * Calls pollEvents to update keyboard and mouse handling
     */
    public void transferBuffersAfterRender() {
        glfwSwapBuffers(windowID); // swap the color buffers
    }

    public void updateWindowTitle() {
        glfwSetWindowTitle(windowID, applicationName + "  |  FPS: " + Math.round(gameTime.getFrameRate()) +
                "  |  Width: " + getWidth() + " Height: " + getHeight());
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
        updateWindowTitle();
    }

    public void setDefaultBackground(Color defaultBackground) {
        this.defaultBackground = defaultBackground;
    }

    public int getWidth() {
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(windowID, w, h);
        return w.get(0);
    }

    public int getHeight() {
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(windowID, w, h);
        return h.get(0);
    }

    public float getAspectRatio() {
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(windowID, w, h);
        return (float) h.get(0) / (float) w.get(0);
    }

    private GLFWWindowSizeCallback recalculateSize() {
        return new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                glViewport(0, 0, width, height);
            }
        };
    }

}
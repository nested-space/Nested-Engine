package com.edenrump.graphic.viewport;

import com.edenrump.graphic.time.Time;
import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;

import java.awt.*;
import java.io.PrintStream;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
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
    private Color defaultBackground;

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

        GLFWVidMode primaryScreenResolution = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (primaryScreenResolution == null)
            throw new RuntimeException("Could not calculate primary screen resolution. Program must exit");

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        int width = (int) Math.round(widthProportion * primaryScreenResolution.width());
        int height = (int) Math.round(heighProportion * primaryScreenResolution.height());

        windowID = glfwCreateWindow(
                width,
                height,
                applicationName,
                fullscreen ? glfwGetPrimaryMonitor() : NULL, NULL);
        if (windowID == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetWindowSizeCallback(windowID, recalculateSize());

        IntBuffer pWidth = BufferUtils.createIntBuffer(1);
        IntBuffer pHeight = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(windowID, pWidth, pHeight);
        glfwSetWindowPos(
                windowID,
                (primaryScreenResolution.width() - pWidth.get(0)) / 2,
                (primaryScreenResolution.height() - pHeight.get(0)) / 2
        );

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

    public void show() {
        glfwShowWindow(windowID);
    }

    /**
     * Closes the window for this Window and clears up callbacks.
     */
    public void terminate() {
        glfwFreeCallbacks(windowID);
        glfwDestroyWindow(windowID);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public boolean isCloseRequested() {
        return glfwWindowShouldClose(windowID);
    }

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

    public void transferBuffersAfterRender() {
        glfwSwapBuffers(windowID);
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
package com.edenrump.graphic.viewport.display;

import com.edenrump.graphic.time.Time;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;

import java.awt.*;
import java.nio.IntBuffer;
import java.util.Objects;

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

    private final Time gameTime = Time.getInstance();
    private long windowID;
    private int width;
    private int height;
    private int posX;
    private int posY = 50;
    private String applicationName = "NestedSpace LWJGL Engine - Test";
    private boolean isFullscreen = false;
    private boolean showFPS = false;
    private boolean showSize = false;
    private Color defaultBackground = Color.BLACK;

    /**
     * Initializes a window ready to be created.
     *
     * @param width  widthProportion of display
     * @param height heighProportion of display
     */
    public Window(int width, int height) {
        // Setup an error callback. The default implementation will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();
        this.width = width;
        this.height = height;
        create();
    }

    /**
     * Creates a new window using current widthProportion, heighProportion, title and background colour
     * Does not take any arguments
     */
    private void create() {

        if (width == 0 || height == 0 || applicationName == null)
            throw new IllegalStateException("Window heighProportion, widthProportion or title has not been initialised");

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        int width = this.width;
        int height = this.height;

        windowID = glfwCreateWindow(
                width,
                height,
                applicationName,
                isFullscreen ? glfwGetPrimaryMonitor() : NULL,
                NULL);

        if (windowID == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetWindowSizeCallback(windowID, recalculateSize());

        updateWindowPosition();

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

    public void setFullScreen(boolean isFullscreen) {
        if (this.isFullscreen == isFullscreen) return;

        GLFWVidMode primary = getVidMode();
        if (isFullscreen) {
            glfwSetWindowMonitor(windowID, glfwGetPrimaryMonitor(),
                    0, 0,
                    primary.width(),
                    primary.height(),
                    primary.refreshRate());
        } else {
            glfwSetWindowMonitor(windowID, glfwGetPrimaryMonitor(),
                    posX, posY,
                    width,
                    height,
                    primary.refreshRate());
        }
        this.isFullscreen = isFullscreen;

    }

    private GLFWVidMode getVidMode() {
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        if (vidMode == null)
            throw new RuntimeException("Could not get primary monitor GLFW window");

        return vidMode;
    }

    public Bounds getBounds() {
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(windowID, w, h);

        IntBuffer x = BufferUtils.createIntBuffer(1);
        IntBuffer y = BufferUtils.createIntBuffer(1);
        glfwGetWindowPos(windowID, x, y);

        return new Bounds(
                x.get(0), x.get(0) + w.get(0),
                y.get(0), y.get(0) + h.get(0));

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
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    public boolean closeNotRequested() {
        return !glfwWindowShouldClose(windowID);
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

    private void updateWindowTitle() {
        String title = applicationName;

        if (showFPS) title += "  |  FPS: " + Math.round(gameTime.getFrameRate());
        if (showSize) title += " | " + getBounds();

        glfwSetWindowTitle(windowID, title);
    }

    private void updateWindowSize() {
        glfwSetWindowSize(windowID, width, height);
    }

    private void updateWindowPosition() {
        glfwSetWindowPos(windowID, posX, posY);
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

    public void setPos(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        updateWindowPosition();
    }

    public void setShowFPS(boolean show) {
        this.showFPS = show;
    }

    public void setShowSize(boolean showSize) {
        this.showSize = showSize;
    }

    public void setSize(int width, int height) {
        if (width <= 0 || height <= 0)
            throw new IllegalArgumentException("Cannot resize window to negative or zero size");
        this.width = width;
        this.height = height;
        updateWindowSize();
    }

    public void setHeight(int height) {
        this.height = height;
        updateWindowSize();
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
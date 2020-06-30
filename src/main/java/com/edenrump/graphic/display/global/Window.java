/*
 * Copyright (c) 2020 Ed Eden-Rump
 *
 * This file is part of Nested Engine.
 *
 * Nested Engine is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nested Engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Nested Engine.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.edenrump.graphic.display.global;

import com.edenrump.graphic.display.ui.Bounds;
import com.edenrump.graphic.time.Time;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;

import java.awt.*;
import java.io.PrintStream;
import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private final Time gameTime = Time.getInstance();
    private long windowID;
    private int width = 1;
    private int height = 1;
    private int posX;
    private int posY = 50;
    private String applicationName = "NestedSpace LWJGL Engine - Test";
    private boolean isFullscreen = false;
    private boolean showFPS = false;
    private boolean showSize = false;
    private Color defaultBackground = Color.BLACK;

    public Window(int width, int height) {
        if (width == 0 || height == 0)
            throw new IllegalStateException("Window heighProportion, widthProportion or title has not been initialised");

        setErrorPrintStream(System.err);
        initialiseGLFW();
        createWindow();
        createCapabilities();
        setSize(width, height);
        setPos(posX, posY);
        prepareForRender();
    }

    private void initialiseGLFW() {
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
    }

    private void createWindow() {
        windowID = glfwCreateWindow(
                width,
                height,
                applicationName,
                isFullscreen ? glfwGetPrimaryMonitor() : NULL,
                NULL);

        if (windowID == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetWindowSizeCallback(windowID, recalculateSize());

        glfwMakeContextCurrent(windowID);

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
    }

    private void createCapabilities(){
        // Enable vertical synchronisation
        glfwSwapInterval(2);

        // This line is critical for LWJGL's interoperation with GLFW's OpenGL context. It makes the bindings available.
        GL.createCapabilities();
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

    private GLFWWindowSizeCallback recalculateSize() {
        return new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                glViewport(0, 0, width, height);
            }
        };
    }

    private GLFWVidMode getVidMode() {
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        if (vidMode == null)
            throw new RuntimeException("Could not get primary monitor GLFW window");

        return vidMode;
    }

    public Bounds getBounds() {
        IntBuffer x = BufferUtils.createIntBuffer(1);
        IntBuffer y = BufferUtils.createIntBuffer(1);
        glfwGetWindowPos(windowID, x, y);

        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(windowID, w, h);

        return new Bounds(x.get(0), y.get(0), w.get(0), h.get(0));
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

    private void updateWindowTitle() {
        String title = applicationName;

        if (showFPS) title += "  |  FPS: " + Math.round(gameTime.getFrameRate());
        if (showSize) title += " | " + getBounds();

        glfwSetWindowTitle(windowID, title);
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
        glfwSetWindowPos(windowID, posX, posY);
    }

    public void setSize(int width, int height) {
        if (width <= 0 || height <= 0)
            throw new IllegalArgumentException("Cannot resize window to negative or zero size");
        this.width = width;
        this.height = height;
        glfwSetWindowSize(windowID, width, height);
    }

    public void setShowFPS(boolean show) {
        this.showFPS = show;
    }

    public void setShowSize(boolean showSize) {
        this.showSize = showSize;
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

    public void setErrorPrintStream(PrintStream printStream){
        GLFWErrorCallback.createPrint(printStream).set();
    }


}
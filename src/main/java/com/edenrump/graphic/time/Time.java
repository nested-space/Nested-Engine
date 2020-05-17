package com.edenrump.graphic.time;

import java.text.DecimalFormat;

import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * This class provides access to glfw time and is responsible for keeping track of the grame rate
 * <p>
 * Must be manually ticked.
 *
 * @author Ed Eden-Rump
 */
public class Time {
    /**
     * Instance of time.
     * Currently does not support more than one context of GLFW (i.e. is a singleton).
     */
    private static Time ourInstance = new Time();
    /**
     * The time in milliseconds of the last frame
     */
    private double lastFrameTime;
    /**
     * The amount of time since Time was last updated
     * This should be done every frame from the game loop.
     */
    private double delta;
    /**
     * The current time (or the time in milliseconds at which Time was last updated)
     */
    private double currentTime;

    private Time() {
    }

    /**
     * Method to return this class.
     *
     * @return singleton Time
     */
    public static Time getInstance() {
        return ourInstance;
    }

    /**
     * Method to update the current, delta and last frame time based on time that's passed since this
     * class was last updated.
     */
    public void updateTime() {
        currentTime = glfwGetTime();
        delta = (currentTime - lastFrameTime) / 1000f;
        lastFrameTime = currentTime;
    }

    /**
     * Method to get the amount of time since Time was last updated
     * If time is updated every frame, this will give frameDelta
     *
     * @return the time between the current time and the last frame
     */
    public double getDeltaTime() {
        return delta;
    }

    /* ****************************************************************************************************************
     * Utility functions
     * ****************************************************************************************************************/

    /**
     * Method to get the frame rate.
     * <p>
     * Assumes Time is updated every frame.
     *
     * @return the frame rate, calculated from delta
     */
    public double getFrameRate() {
        return (1 / delta) / 1000;
    }

    /**
     * Utility method to print the frame rate nicely.
     *
     * @param decimalPlaces the number of decimal places you want the frame rate to.
     * @return nicely formatted string explaining the frame rate
     */
    public String getPrintFrameRate(int decimalPlaces) {

        StringBuilder pattern = new StringBuilder("###.");

        for (int i = 0; i < decimalPlaces; i++) {
            pattern.append("#");
        }

        DecimalFormat decimalFormat = new DecimalFormat(pattern.toString());
        return decimalFormat.format(getFrameRate());
    }

    /**
     * Method provides access to glfwGetTime. GLFW must have a current context or this will be invalid.
     *
     * @return the current time in milliseonds.
     */
    public double getCurrentTimeMillis() {
        if (glfwGetCurrentContext() == 0)
            throw new IllegalStateException("GLFW not initialised - unable to get current time");
        return glfwGetTime() * 1000;
    }
}

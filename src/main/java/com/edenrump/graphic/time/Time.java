package com.edenrump.graphic.time;

import java.text.DecimalFormat;

import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

/*
 * Copyright (c) 2020 Ed Eden-Rump
 *     This file is part of Nested Engine.
 *
 *     Nested Engine is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Nested Engine is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Nested Engine.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     @Author Ed Eden-Rump
 *
 */

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
    private static final Time ourInstance = new Time();
    /**
     * The time in milliseconds of the last frame
     */
    private double lastFrameTime;
    /**
     * The amount of time since Time was last updated
     * This should be done every frame from the game loop.
     */
    private double delta;

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
        double currentTime = glfwGetTime();
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
        DecimalFormat decimalFormat = new DecimalFormat("###." + "#".repeat(Math.max(0, decimalPlaces)));
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

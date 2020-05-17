package com.edenrump.math.util;

/**
 * This class contains common mathematical transformations used in 2D and 3D rendering
 *
 * This class primarily holds operations conducted on scalar values (i.e. float/int) rather than matrices or vectors.
 *
 * @author Ed Eden-Rump
 */
public class UnityComputation {

    public static float clamp(float value, float min, float max) {
        return Math.max(Math.min(value, max), min);
    }

}

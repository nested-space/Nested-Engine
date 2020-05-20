package com.edenrump.math.calculations;

/**
 * This class holds useful operations conducted on scalar values
 *
 * @author Ed Eden-Rump
 */
public class Scalar {

    public static float clamp(float value, float min, float max) {
        return Math.max(Math.min(value, max), min);
    }

}

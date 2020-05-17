package com.edenrump.math.util;

/**
 * This class provides useful methods to use on arrays
 */
public class ArrayUtils {

    public static float[] concatenateArrays(float[]... floatArrays) {
        //calculate number of floats
        int floats = 0;
        for (float[] floatArray : floatArrays) {
            floats += floatArray.length;
        }

        float[] concatArray = new float[floats];

        int floatsAdded = 0;
        for (float[] floatArray : floatArrays) {
            System.arraycopy(floatArray, 0, concatArray, floatsAdded, floatArray.length);
            floatsAdded += floatArray.length;
        }

        return concatArray;
    }

    public static int[] concatenateArrays(int[]... intArrays) {
        //calculate number of ints
        int ints = 0;
        for (int[] intArray : intArrays) {
            ints += intArray.length;
        }

        int[] concatArray = new int[ints];

        int intsAdded = 0;
        for (int[] intArray : intArrays) {
            System.arraycopy(intArray, 0, concatArray, intsAdded, intArray.length);
            intsAdded += intArray.length;
        }

        return concatArray;
    }


}

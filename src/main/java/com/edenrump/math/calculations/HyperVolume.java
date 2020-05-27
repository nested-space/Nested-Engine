package com.edenrump.math.calculations;

import com.edenrump.math.arrays.ColumnVector;
import com.edenrump.math.arrays.SquareMatrix;

public class HyperVolume {

    /**
     * Creates a translation matrix. Similar to
     * <code>glTranslate(x, y, z)</code>.
     *
     * @param x x coordinate of translation vector
     * @param y y coordinate of translation vector
     * @param z z coordinate of translation vector
     * @return Translation matrix
     */
    public static SquareMatrix translate(float x, float y, float z) {
        return new SquareMatrix(new float[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                x, y, z, 1});
    }

    /**
     * Creates a rotation matrix. Similar to
     * <code>glRotate(angle, x, y, z)</code>.
     *
     * @param angle Angle of rotation in degrees
     * @param x     x coordinate of the rotation vector
     * @param y     y coordinate of the rotation vector
     * @param z     z coordinate of the rotation vector
     * @return Rotation matrix
     */
    public static SquareMatrix rotate(float angle, float x, float y, float z) {
        float c = (float) Math.cos(Math.toRadians(angle));
        float s = (float) Math.sin(Math.toRadians(angle));
        ColumnVector vec = new ColumnVector(x, y, z);
        if (vec.length() != 1f) {
            vec = vec.normalize();
            x = vec.getValues()[0];
            y = vec.getValues()[1];
            z = vec.getValues()[2];
        }

        ColumnVector c0 = new ColumnVector(
                x * x * (1f - c) + c,
                y * x * (1f - c) + z * s,
                x * z * (1f - c) - y * s,
                0
        );

        ColumnVector c1 = new ColumnVector(
                x * y * (1f - c) - z * s,
                y * y * (1f - c) + c,
                y * z * (1f - c) + x * s,
                0
        );

        ColumnVector c2 = new ColumnVector(
                x * z * (1f - c) + y * s,
                y * z * (1f - c) - x * s,
                z * z * (1f - c) + c,
                0
        );

        ColumnVector c3 = new ColumnVector(0, 0, 0, 1);
        return new SquareMatrix(c0, c1, c2, c3);
    }

    /**
     * Creates a scaling matrix. Similar to <code>glScale(x, y, z)</code>.
     *
     * @param x Scale factor along the x coordinate
     * @param y Scale factor along the y coordinate
     * @param z Scale factor along the z coordinate
     * @return Scaling matrix
     */
    public static SquareMatrix scale(float x, float y, float z) {
        return new SquareMatrix(new float[]{
                x, 0, 0, 0,
                0, y, 0, 0,
                0, 0, z, 0,
                0, 0, 0, 1
        });
    }

    public static SquareMatrix translate(ColumnVector translation) {
        return translate(
                translation.getValues()[0],
                translation.getValues()[1],
                translation.getValues()[2]
        );
    }
}

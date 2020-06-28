package com.edenrump.math.util;

import com.edenrump.math.arrays.ColumnVector;
import com.edenrump.math.arrays.SquareMatrix;

/**
 * This class contains a number of utility methods that are useful to modifying or creating matrices and vectors in
 * 3D graphics
 *
 * @author Ed Eden-Rump
 */
public class Volume {

    /**
     * Method to rotate a vector vectors about an axis by angle theta
     *
     * @param vec   the vector to be rotated
     * @param axis  the axis that defines the rotation
     * @param theta the angle by which to rotate the vector, in radians
     * @return the new rotated vector
     */
    public static ColumnVector rotateVectorCC(ColumnVector vec,
                                              ColumnVector axis,
                                              double theta) {
        checkDimensionality(vec, axis);

        double x, y, z;
        double u, v, w;
        x = vec.getValues()[0];
        y = vec.getValues()[1];
        z = vec.getValues()[2];
        u = axis.getValues()[0];
        v = axis.getValues()[1];
        w = axis.getValues()[2];
        final double uxvywz = u * x + v * y + w * z;
        float xPrime = (float) (u * uxvywz * (1d - Math.cos(theta))
                + x * Math.cos(theta)
                + (-w * y + v * z) * Math.sin(theta));
        float yPrime = (float) (v * uxvywz * (1d - Math.cos(theta))
                + y * Math.cos(theta)
                + (w * x - u * z) * Math.sin(theta));
        float zPrime = (float) (w * uxvywz * (1d - Math.cos(theta))
                + z * Math.cos(theta)
                + (-v * x + u * y) * Math.sin(theta));
        return new ColumnVector(xPrime, yPrime, zPrime);
    }

    /**
     * Calculates the normal of the triangle made from the 3 vertices. The vertices must be specified in counter-clockwise order.
     *
     * @param vertex0 xyz coordinates of first vertex of triangle
     * @param vertex1 xyz coordinates of second vertex of triangle
     * @param vertex2 xyz coordinates of third vertex of triangle
     * @return normalised normal vector for triangle
     */
    public static ColumnVector calcNormal(ColumnVector vertex0,
                                          ColumnVector vertex1,
                                          ColumnVector vertex2) {
        checkDimensionality(vertex0, vertex1, vertex2);

        ColumnVector tangentA = vertex1.subtract(vertex0);
        ColumnVector tangentB = vertex2.subtract(vertex0);
        ColumnVector normal = tangentA.cross(tangentB);
        normal.normalize();
        return normal;
    }

    /**
     * Creates a 4D transformation matrix from separate values for translation, rotation and scale
     *
     * @param translation the xyz translation of the object
     * @param rotation    the xyz roataion of the object
     * @param scale       the xyz scale of the object
     * @return 4D transformation matrix
     */
    public static SquareMatrix createTransformationMatrix(ColumnVector translation,
                                                          ColumnVector rotation,
                                                          ColumnVector scale) {
        checkDimensionality(translation, rotation, scale);

        SquareMatrix transformationMatrix = new SquareMatrix(4);

        SquareMatrix translationMatrix = createTranslationMatrix(
                translation.getValues()[0],
                translation.getValues()[1],
                translation.getValues()[2]
        );

        transformationMatrix = transformationMatrix.multiply(translationMatrix);

        //rotate about each cartesian axis
        transformationMatrix = transformationMatrix.multiply(createRotationMatrix(rotation.getValues()[0], 1, 0, 0));
        transformationMatrix = transformationMatrix.multiply(createRotationMatrix(rotation.getValues()[1], 0, 1, 0));
        transformationMatrix = transformationMatrix.multiply(createRotationMatrix(rotation.getValues()[2], 0, 0, 1));

        //scale
        transformationMatrix = transformationMatrix.multiply(createScaleMatrix(scale.getValues()[0], scale.getValues()[1], scale.getValues()[3]));
        return transformationMatrix;
    }

    private static void checkDimensionality(ColumnVector... vectors) {
        for (ColumnVector vector : vectors) {
            if (vector.getDimensions() != 3)
                throw new IllegalArgumentException("Cannot do volume computations on non-3d vectors");
        }
    }


    public static ColumnVector convertCartesianCoordinateToPolar(ColumnVector cartesianCoordinates) {
        float x = cartesianCoordinates.getValue(0);
        float y = cartesianCoordinates.getValue(1);
        float z = cartesianCoordinates.getValue(2);
        float r = cartesianCoordinates.length();
        float azimuth = (float) Math.atan(y / x);
        float inclination = (float) Math.acos(z / r);
        return new ColumnVector(r, inclination, azimuth);
    }

    public static ColumnVector convertPolarCoordinateToCartesian(ColumnVector polarCoordinates) {
        float r = polarCoordinates.getValue(0);
        float inclinationAngle = polarCoordinates.getValue(1);
        float azimuthAngle = polarCoordinates.getValue(2);
        float x = (float) (r * Math.cos(azimuthAngle) * Math.sin(inclinationAngle));
        float y = (float) (r * Math.sin(azimuthAngle) * Math.sin(inclinationAngle));
        float z = (float) (r * Math.cos(inclinationAngle));
        return new ColumnVector(x, y, z);
    }

    /**
     * Creates a translation matrix. Similar to
     * <code>glTranslate(x, y, z)</code>.
     *
     * @param x x coordinate of translation vector
     * @param y y coordinate of translation vector
     * @param z z coordinate of translation vector
     * @return Translation matrix
     */
    public static SquareMatrix createTranslationMatrix(float x, float y, float z) {
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
    public static SquareMatrix createRotationMatrix(float angle, float x, float y, float z) {
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
    public static SquareMatrix createScaleMatrix(float x, float y, float z) {
        return new SquareMatrix(new float[]{
                x, 0, 0, 0,
                0, y, 0, 0,
                0, 0, z, 0,
                0, 0, 0, 1
        });
    }

    public static SquareMatrix createTranslationMatrix(ColumnVector translation) {
        return createTranslationMatrix(
                translation.getValues()[0],
                translation.getValues()[1],
                translation.getValues()[2]
        );
    }
}

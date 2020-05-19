package com.edenrump.math.util;

import com.edenrump.math.arrays.ColumnVector;
import com.edenrump.math.arrays.Matrix4f;

/**
 * This class represents a number of utility methods that are useful to modifying or creating matrices and vectors in
 * 3D graphics
 *
 * @author Ed Eden-Rump
 */
public class VolumeComputation {

    /**
     * Method to rotate a vector vectors about an axis by angle theta
     *
     * @param vec   the vector to be rotated
     * @param axis  the axis that defines the rotation
     * @param theta the angle by which to rotate the vector, in radians
     * @return the new rotated vector
     */
    public static ColumnVector rotateVectorCC(ColumnVector vec, ColumnVector axis, double theta) {
        checkDimensionality(vec, axis);

        double x, y, z;
        double u, v, w;
        x = vec.getValues()[0];
        y = vec.getValues()[1];
        z = vec.getValues()[2];
        u = axis.getValues()[0];
        v = axis.getValues()[1];
        w = axis.getValues()[2];
        float xPrime = (float) (u * (u * x + v * y + w * z) * (1d - Math.cos(theta))
                + x * Math.cos(theta)
                + (-w * y + v * z) * Math.sin(theta));
        float yPrime = (float) (v * (u * x + v * y + w * z) * (1d - Math.cos(theta))
                + y * Math.cos(theta)
                + (w * x - u * z) * Math.sin(theta));
        float zPrime = (float) (w * (u * x + v * y + w * z) * (1d - Math.cos(theta))
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
    public static ColumnVector calcNormal(ColumnVector vertex0, ColumnVector vertex1, ColumnVector vertex2) {
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
    public static Matrix4f createTransformationMatrix(ColumnVector translation,
                                                      ColumnVector rotation,
                                                      ColumnVector scale) {
        checkDimensionality(translation, rotation, scale);

        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();

        //translate matrix
        matrix = Matrix4f.translate(translation.getValues()[0], translation.getValues()[1], translation.getValues()[2]);

        //rotate about each cartesian axis
        matrix = matrix.multiply(Matrix4f.rotate((float) Math.toRadians(rotation.getValues()[0]), 1, 0, 0));
        matrix = matrix.multiply(Matrix4f.rotate((float) Math.toRadians(rotation.getValues()[1]), 0, 1, 0));
        matrix = matrix.multiply(Matrix4f.rotate((float) Math.toRadians(rotation.getValues()[2]), 0, 0, 1));

        //scale
        matrix = matrix.multiply(Matrix4f.scale(scale.getValues()[0], scale.getValues()[1], scale.getValues()[3]));
        return matrix;
    }

    private static void checkDimensionality(ColumnVector... vectors) {
        for (ColumnVector vector : vectors) {
            if (vector.getDimensions() != 3)
                throw new IllegalArgumentException("Cannot do volume computations on non-3d vectors");
        }
    }


}

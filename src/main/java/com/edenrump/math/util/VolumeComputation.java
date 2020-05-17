package com.edenrump.math.util;

import com.edenrump.math.arrays.Matrix4f;
import com.edenrump.math.arrays.Vector3f;

/**
 * This class represents a number of utility methods that are useful to modifying or creating matrices and vectors in
 * 3D graphics
 *
 * @author Ed Eden-Rump
 */
public class VolumeComputation {

    /**
     * Method to rotate a vector vectors about an axis by angle theta
     * @param vec the vector to be rotated
     * @param axis the axis that defines the rotation
     * @param theta the angle by which to rotate the vector, in radians
     * @return the new rotated vector
     */
    public static Vector3f rotateVectorCC(Vector3f vec, Vector3f axis, double theta) {
        double x, y, z;
        double u, v, w;
        x = vec.x;
        y = vec.y;
        z = vec.z;
        u = axis.x;
        v = axis.y;
        w = axis.z;
        float xPrime = (float) (u * (u * x + v * y + w * z) * (1d - Math.cos(theta))
                + x * Math.cos(theta)
                + (-w * y + v * z) * Math.sin(theta));
        float yPrime = (float) (v * (u * x + v * y + w * z) * (1d - Math.cos(theta))
                + y * Math.cos(theta)
                + (w * x - u * z) * Math.sin(theta));
        float zPrime = (float) (w * (u * x + v * y + w * z) * (1d - Math.cos(theta))
                + z * Math.cos(theta)
                + (-v * x + u * y) * Math.sin(theta));
        return new Vector3f(xPrime, yPrime, zPrime);
    }

    /**
     * Calculates the normal of the triangle made from the 3 vertices. The vertices must be specified in counter-clockwise order.
     *
     * @param vertex0 xyz coordinates of first vertex of triangle
     * @param vertex1 xyz coordinates of second vertex of triangle
     * @param vertex2 xyz coordinates of third vertex of triangle
     * @return normalised normal vector for triangle
     */
    public static Vector3f calcNormal(Vector3f vertex0, Vector3f vertex1, Vector3f vertex2) {
        Vector3f tangentA = vertex1.subtract(vertex0);
        Vector3f tangentB = vertex2.subtract(vertex0);
        Vector3f normal = tangentA.cross(tangentB);
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
    public static Matrix4f createTransformationMatrix(Vector3f translation,
                                                      Vector3f rotation,
                                                      Vector3f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();

        //translate matrix
        matrix = Matrix4f.translate(translation.x, translation.y, translation.z);

        //rotate about each cartesian axis
        matrix = matrix.multiply(Matrix4f.rotate((float) Math.toRadians(rotation.x), 1, 0, 0));
        matrix = matrix.multiply(Matrix4f.rotate((float) Math.toRadians(rotation.y), 0, 1, 0));
        matrix = matrix.multiply(Matrix4f.rotate((float) Math.toRadians(rotation.z), 0, 0, 1));

        //scale
        matrix = matrix.multiply(Matrix4f.scale(scale.x, scale.y, scale.z));
        return matrix;
    }


}

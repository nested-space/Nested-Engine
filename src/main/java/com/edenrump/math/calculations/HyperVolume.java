package com.edenrump.math.calculations;

import com.edenrump.math.arrays.ColumnVector;
import com.edenrump.math.arrays.SquareMatrix;

public class HyperVolume {

    /**
     * Creates a orthographic projection matrix. Similar to
     * <code>glOrtho(left, right, bottom, top, near, far)</code>.
     *
     * @param left   Coordinate for the left vertical clipping pane
     * @param right  Coordinate for the right vertical clipping pane
     * @param bottom Coordinate for the bottom horizontal clipping pane
     * @param top    Coordinate for the bottom horizontal clipping pane
     * @param near   Coordinate for the near depth clipping pane
     * @param far    Coordinate for the far depth clipping pane
     * @return Orthographic matrix
     */
    public static SquareMatrix orthographic(float left, float right, float bottom, float top, float near, float far) {
        float tx = -(right + left) / (right - left);
        float ty = -(top + bottom) / (top - bottom);
        float tz = -(far + near) / (far - near);
        ColumnVector c0 = new ColumnVector(2f / (right - left), 0, 0, 0);
        ColumnVector c1 = new ColumnVector(0, 2f / (right - left), 0, 0);
        ColumnVector c2 = new ColumnVector(0, 0, -2f / (far - near), 0);
        ColumnVector c3 = new ColumnVector(tx, ty, tz, 1);

        return new SquareMatrix(c0, c1, c2, c3);
    }

    /**
     * Creates a perspective projection matrix. Similar to
     * <code>glFrustum(left, right, bottom, top, near, far)</code>.
     *
     * @param left   Coordinate for the left vertical clipping pane
     * @param right  Coordinate for the right vertical clipping pane
     * @param bottom Coordinate for the bottom horizontal clipping pane
     * @param top    Coordinate for the bottom horizontal clipping pane
     * @param near   Coordinate for the near depth clipping pane, must be
     *               positive
     * @param far    Coordinate for the far depth clipping pane, must be
     *               positive
     * @return Perspective matrix
     */
    public static SquareMatrix frustum(float left, float right, float bottom, float top, float near, float far) {
        float a = (right + left) / (right - left);
        float b = (top + bottom) / (top - bottom);
        float c = -(far + near) / (far - near);
        float d = -(2f * far * near) / (far - near);

        ColumnVector c0 = new ColumnVector((2f * near) / (right - left), 0, 0, 0);
        ColumnVector c1 = new ColumnVector(0, (2f * near) / (top - bottom), 0, 0);
        ColumnVector c2 = new ColumnVector(a, b, c, -1f);
        ColumnVector c3 = new ColumnVector(0, 0, d, 0);

        return new SquareMatrix(c0, c1, c2, c3);
    }

    /**
     * Creates a perspective projection matrix. Similar to
     * <code>gluPerspective(fovy, aspec, zNear, zFar)</code>.
     *
     * @param fovy   Field of view angle in degrees
     * @param aspect The aspect ratio is the ratio of width to height
     * @param near   Distance from the viewer to the near clipping plane, must
     *               be positive
     * @param far    Distance from the viewer to the far clipping plane, must be
     *               positive
     * @return Perspective matrix
     */
    public static SquareMatrix perspective(float fovy, float aspect, float near, float far) {
        float f = (float) (1f / Math.tan(Math.toRadians(fovy) / 2f));
        ColumnVector c0 = new ColumnVector(f / aspect, 0, 0, 0);
        ColumnVector c1 = new ColumnVector(0, f, 0, 0);
        ColumnVector c2 = new ColumnVector(0, 0, (far + near) / (near - far), -1f);
        ColumnVector c3 = new ColumnVector(0, 0, (2f * far * near) / (near - far), 0f);
        return new SquareMatrix(c0, c1, c2, c3);
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
    public static SquareMatrix translate(float x, float y, float z) {
        return new SquareMatrix(new float[]{
                0, 0, 0, 0,
                0f, 0, 0, 0,
                0, 0, 0, 0,
                x, y, z, 0});
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

        ColumnVector c3 = new ColumnVector(0, 0, 0, 0);
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
                0, 0, 0, 0
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

package com.edenrump.graphic.geom;

import com.edenrump.math.arrays.ColumnVector;
import com.edenrump.math.arrays.SquareMatrix;

public interface Projection {

    SquareMatrix getProjectionMatrix();

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


}

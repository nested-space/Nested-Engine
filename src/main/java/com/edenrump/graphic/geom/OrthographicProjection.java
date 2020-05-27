package com.edenrump.graphic.geom;

import com.edenrump.math.arrays.ColumnVector;
import com.edenrump.math.arrays.SquareMatrix;

public class OrthographicProjection {

    private float right = 1;
    private float left = -1;
    private float top = 1;
    private float bottom = -1;
    private float far = 1000;
    private float near = 0.1f;

    private SquareMatrix cacheProjectionMatrix;

    private OrthographicProjection(){}

    public OrthographicProjection(float left, float right, float bottom, float top, float near, float far){
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.top = top;
        this.near = near;
        this.far = far;
        createProjectionMatrix();
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public float getFar() {
        return far;
    }

    public void setFar(float far) {
        this.far = far;
    }

    public float getNear() {
        return near;
    }

    public void setNear(float near) {
        this.near = near;
    }

    void createProjectionMatrix(){
        float tx = -(right + left) / (right - left);
        float ty = -(top + bottom) / (top - bottom);
        float tz = -(far + near) / (far - near);
        ColumnVector c0 = new ColumnVector(2f / (right - left), 0, 0, 0);
        ColumnVector c1 = new ColumnVector(0, 2f / (right - left), 0, 0);
        ColumnVector c2 = new ColumnVector(0, 0, -2f / (far - near), 0);
        ColumnVector c3 = new ColumnVector(tx, ty, tz, 1);

        this.cacheProjectionMatrix = new SquareMatrix(c0, c1, c2, c3);
    }

    public SquareMatrix getProjectionMatrix() {
        return cacheProjectionMatrix;
    }
}

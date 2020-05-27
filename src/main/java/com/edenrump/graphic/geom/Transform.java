package com.edenrump.graphic.geom;

import com.edenrump.math.arrays.ColumnVector;
import com.edenrump.math.arrays.SquareMatrix;
import com.edenrump.math.calculations.HyperVolume;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class Transform {

    private ColumnVector translation;
    private ColumnVector rotation;
    private ColumnVector scale;

    public Transform() {
        translation = new ColumnVector(4);
        rotation = new ColumnVector(0, 0, 0, 0);
        scale = new ColumnVector(1, 1, 1, 0);
    }

    public ColumnVector getTranslation() {
        return translation;
    }

    public ColumnVector getRotation() {
        return rotation;
    }

    public ColumnVector getScale() {
        return scale;
    }

    public void rotate(float x, float y, float z) {
        rotation = rotation.add(new ColumnVector(x, y, z, 0));
    }

    protected void rotateX(float x) {
        rotate(x, 0, 0);
    }

    protected void rotateY(float y) {
        rotate(0, y, 0);
    }

    protected void rotateZ(float z) {
        rotate(0, 0, z);
    }

    public void scale(float x, float y, float z) {
        float[] oldScale = scale.getValues();
        scale = new ColumnVector(
                x * oldScale[0],
                y * oldScale[1],
                z * oldScale[2],
                1);
    }

    protected void scaleX(float x) {
        scale(x, 0, 0);
    }

    protected void scaleY(float y) {
        scale(0, y, 0);
    }

    protected void scaleZ(float z) {
        scale(0, 0, z);
    }

    public void translate(float x, float y, float z) {
        translation = translation.add(new ColumnVector(x, y, z, 0));
    }

    protected void translateX(float x) {
        translate(x, 0, 0);
    }

    protected void translateY(float y) {
        translate(0, y, 0);
    }

    protected void translateZ(float z) {
        translate(0, 0, z);
    }

    public FloatBuffer getTransformationMatrix() {
        SquareMatrix transformationMatrix = new SquareMatrix(4);

        SquareMatrix txyz = HyperVolume.translate(translation);
        SquareMatrix rx = HyperVolume.rotate(rotation.getValues()[0], 1, 0, 0);
        SquareMatrix ry = HyperVolume.rotate(rotation.getValues()[1], 0, 1, 0);
        SquareMatrix rz = HyperVolume.rotate(rotation.getValues()[2], 0, 0, 1);
        SquareMatrix s = HyperVolume.scale(scale.getValues()[0], scale.getValues()[1], scale.getValues()[2]);
        transformationMatrix = transformationMatrix.multiply(s).multiply(rx).multiply(ry).multiply(rz).multiply(txyz);

        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        transformationMatrix.storeMatrixInBuffer(buffer);
        buffer.flip();
        return buffer;
    }
}

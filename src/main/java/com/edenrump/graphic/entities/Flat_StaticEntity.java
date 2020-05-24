package com.edenrump.graphic.entities;

import com.edenrump.graphic.geom.Transform;
import com.edenrump.graphic.mesh.Flat_StaticMesh;
import com.edenrump.graphic.openGL_gpu.Uniform;

public class Flat_StaticEntity implements Renderable {

    Flat_StaticMesh mesh;
    Transform transform;
    Uniform transformationMatrix = new Uniform(0, 0, "null");

    public Flat_StaticEntity(Flat_StaticMesh mesh) {
        this.mesh = mesh;
        transform = new Transform();
    }

    public void setTransformationUniform(Uniform transformationMatrix) {
        this.transformationMatrix = transformationMatrix;
    }

    @Override
    public int getID() {
        return mesh.getVAO_ID();
    }

    @Override
    public void prepare() {
        mesh.bindVAO();
    }

    @Override
    public void update() {
        mesh.enableAttributes();
        transformationMatrix.asUniformMatrix().update_4x4(transform.getTransformationMatrix());
    }

    @Override
    public void finish() {
        mesh.disableAttributes();
    }

    @Override
    public int getDrawType() {
        return mesh.getDrawType();
    }

    @Override
    public int getElements() {
        return mesh.getElements();
    }

    public void scale(float x, float y, float z) {
        transform.scale(x, y, z);
    }

    public void translate(float x, float y, float z) {
        transform.translate(x, y, z);
    }
}

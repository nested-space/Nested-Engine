package com.edenrump.graphic.entities;

import com.edenrump.graphic.geom.Transform;
import com.edenrump.graphic.components.Mesh;
import com.edenrump.graphic.openGL_gpu.Uniform;

public class GUI_StaticEntity implements Renderable {

    Mesh mesh;
    Transform transform;
    Uniform transformationMatrix = new Uniform(0, 0, "null");

    public GUI_StaticEntity(Mesh mesh) {
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
        if(transformationMatrix.getName()!="null"){
            transformationMatrix.asUniformMatrix().update_4x4(transform.getTransformationMatrix());
        }
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

    public void rotate(float x, float y, float z) {
        transform.rotate(x, y, z);
    }

    public float[] getScale() {
        return transform.getScale().getValues();
    }

    public float[] getTranslation(){
        return transform.getTranslation().getValues();
    }

    public float[] getRotation(){
        return transform.getRotation().getValues();
    }
}

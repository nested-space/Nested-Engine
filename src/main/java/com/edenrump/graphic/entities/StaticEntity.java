package com.edenrump.graphic.entities;

import com.edenrump.math.geom.Transform;
import com.edenrump.graphic.mesh.GPUMesh;
import com.edenrump.gpu.Uniform;

/*
 * Copyright (c) 2020 Ed Eden-Rump
 *     This file is part of Nested Engine.
 *
 *     Nested Engine is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Nested Engine is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Nested Engine.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     @Author Ed Eden-Rump
 *
 */

public class StaticEntity implements Renderable {

    final GPUMesh mesh;
    final Transform transform;
    Uniform transformationMatrix = null;

    public StaticEntity(GPUMesh mesh) {
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
        if (transformationMatrix == null) return;

        if (transformationMatrix.getName() != "null") {
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
    public int getNumberOfElements() {
        return mesh.getNumberOfElements();
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

    public Transform getTransform() {
        return transform;
    }
}

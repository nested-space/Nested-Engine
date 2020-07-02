/*
 * Copyright (c) 2020 Ed Eden-Rump
 *
 * This file is part of Nested Engine.
 *
 * Nested Engine is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nested Engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Nested Engine.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.edenrump.graphic.display.ui;

import com.edenrump.gpu.objects.Uniform;
import com.edenrump.graphic.entities.Renderable;
import com.edenrump.graphic.mesh.GPUMesh;
import com.edenrump.math.geom.Transform;

/**
 * @author Ed Eden-Rump
 * @created 30/06/2020 - 17:45
 * @project Nested Engine
 **/
public abstract class Component extends Container implements Renderable {

    private final GPUMesh mesh;
    private Transform transform;
    private final Uniform transformationUniform;

    private Component() {
        super();
        ComponentGPUSupport staff = ComponentGPUSupport.getInstance();

        mesh = staff.getSquareMesh();
        transformationUniform = staff.getTransformationMatrix();
        transform = new Transform();
    }

    private void updateTransformFromConstraints() {
        //TODO FIXME -- convert layout constraints to transform.
        transform = new Transform();
    }

    @Override
    public void prepare() {
        mesh.bindVAO();
    }

    @Override
    public void update() {
        mesh.enableAttributes();
        if (transformationUniform == null) return;

        if (transformationUniform.getName() != "null") {
            transformationUniform.asUniformMatrix().update_4x4(transform.getTransformationMatrix());
        }
    }

    @Override
    public void finish() {
        mesh.disableAttributes();
    }

    @Override
    public GPUMesh getMesh() {
        return mesh;
    }

    public void scale(float x, float y) {
        transform.scale(x, y, 1);
    }

    public void translate(float x, float y) {
        transform.translate(x, y, 0);
    }

    public void rotate(float x, float y) {
        transform.rotate(x, y, 0);
    }
}

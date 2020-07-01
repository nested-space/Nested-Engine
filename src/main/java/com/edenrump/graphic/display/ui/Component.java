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

    Container parent;
    GPUMesh mesh;
    Uniform transformationMatrix;

    public Component(){
        transformationMatrix = new Uniform()
    }

    private Transform createTransformFromConstraints(){
        //FIXME
        return new Transform();
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

    }

    @Override
    public void finish() {

    }

    @Override
    public int getDrawType() {
        return 0;
    }

    @Override
    public int getNumberOfElements() {
        return 0;
    }
}

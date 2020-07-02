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

package com.edenrump.graphic.render;

import com.edenrump.graphic.entities.Renderable;
import com.edenrump.graphic.mesh.GPUMesh;
import com.edenrump.graphic.shaders.ShaderProgram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;

/**
 * This class holds all the information required to render a mesh onto screen in OpenGL
 * <p>
 * It requires a working shader program, and should be loaded with all of the attributes you need to render the mesh.
 * <p>
 * All meshes registered with this an instance of a Renderer should have the same attributes, configuration and
 * rendering requirements
 * <p>
 * It can handle drawing with and without texture coordinates.
 */
public class StaticRenderer implements GenericRenderer {

    private final ShaderProgram shaderProgram;
    private final Map<Integer, List<Renderable>> vaoIDMeshMap = new HashMap<>();

    public StaticRenderer(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    @Override
    public void render() {
        prepare();
        for (Integer id : vaoIDMeshMap.keySet()) {
            List<Renderable> allMeshInstances = vaoIDMeshMap.get(id);
            allMeshInstances.get(0).prepare();
            for (Renderable renderable : allMeshInstances) {
                renderable.update();

                GPUMesh mesh = renderable.getMesh();
                glDrawElements(mesh.getDrawType(), mesh.getNumberOfElements(), GL_UNSIGNED_INT, 0);

                renderable.finish();
            }
            Renderable.unbind();
        }
    }

    @Override
    public void prepare() {
        shaderProgram.use();
    }

    @Override
    public void cleanUp() {
        shaderProgram.delete();
    }

    public void addMesh(Renderable renderable) {
        GPUMesh mesh = renderable.getMesh();
        if (!vaoIDMeshMap.containsKey(mesh.getVAO_ID())) {
            List<Renderable> newMeshList = new ArrayList<>();
            vaoIDMeshMap.put(
                    mesh.getVAO_ID(),
                    newMeshList);
        }
        vaoIDMeshMap.get(mesh.getVAO_ID()).add(renderable);
    }

    public void removeMesh(Renderable renderable) {
        GPUMesh mesh = renderable.getMesh();
        vaoIDMeshMap.getOrDefault(mesh.getVAO_ID(), new ArrayList<>()).remove(renderable);
    }
}

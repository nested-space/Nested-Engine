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
import com.edenrump.graphic.mesh.GPUMesh;
import com.edenrump.graphic.shaders.Shader;
import com.edenrump.graphic.shaders.ShaderProgram;

import static org.lwjgl.opengl.GL15C.GL_STATIC_DRAW;

/**
 * @author Ed Eden-Rump
 * @created 30/06/2020 - 18:34
 * @project Nested Engine
 **/
public class ComponentGPUSupport {

    private static ComponentGPUSupport INSTANCE;

    private final ShaderProgram shaderProgram;
    private final Uniform transformationMatrix;
    private final GPUMesh squareMesh;

    private ComponentGPUSupport() {
        shaderProgram = createShader();
        transformationMatrix = getUITransformationMatrixUniform();
        squareMesh = createSquareMesh();
    }

    public static ComponentGPUSupport getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ComponentGPUSupport();
        }

        return INSTANCE;
    }

    public static int getDrawType(){
        return GL_STATIC_DRAW;
    }

    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    public Uniform getTransformationMatrix() {
        return transformationMatrix;
    }

    public GPUMesh getSquareMesh() {
        return squareMesh;
    }

    private Uniform getUITransformationMatrixUniform() {
        Uniform transformation = shaderProgram.getUniform("modelMatrix");

        if (transformation.getLocation() == -1)
            throw new RuntimeException("Transformation matrix is not present in GLSL for Component initialisation");

        return transformation;
    }

    private GPUMesh createSquareMesh() {
        float[] positions = new float[]{
                -1f, 1f, 1f,      //v0
                -1f, -1f, 1f,   //v1
                1f, -1f, 1f,   //v2
                1f, 1f, 1f     //v3
        };
        int[] indices = new int[]{
                0, 1, 3,//top left triangle (v0, v1, v3)
                3, 1, 2//bottom right triangle (v3, v1, v2)
        };

        GPUMesh mesh = new GPUMesh(3);
        mesh.setPositions(positions, indices);
        mesh.setDrawType(GL_STATIC_DRAW);
        return mesh;
    }

    private ShaderProgram createShader() {
        String VERTEX_FILE_LOCATION = "src/resources/shaderCode/ComponentShader.vert";
        String FRAGMENT_FILE_LOCATION = "src/resources/shaderCode/ComponentShader.frag";

        ShaderProgram shaderProgram = new ShaderProgram();
        Shader v = Shader.loadShader(Shader.VERTEX, VERTEX_FILE_LOCATION);
        Shader f = Shader.loadShader(Shader.FRAGMENT, FRAGMENT_FILE_LOCATION);
        shaderProgram.attachShaders(v, f);
        shaderProgram.link();
        v.delete();
        f.delete();

        return shaderProgram;
    }
}

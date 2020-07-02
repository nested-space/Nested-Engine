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

package com.edenrump.gizmos;

import com.edenrump.gpu.data.Std140Compatible;
import com.edenrump.gpu.data.std140ColumnVector;
import com.edenrump.graphic.entities.StaticEntity;
import com.edenrump.gpu.objects.UniformBlockBuffer;
import com.edenrump.graphic.mesh.GPUMesh;
import com.edenrump.graphic.render.StaticRenderer;
import com.edenrump.graphic.shaders.Shader;
import com.edenrump.graphic.shaders.ShaderProgram;
import com.edenrump.graphic.time.Time;
import com.edenrump.graphic.display.ui.Bounds;
import com.edenrump.graphic.display.global.Window;

import java.awt.*;

public class UniformRoundedCornersTest {

    static final float[] positions = new float[]{
            1f, 1f, 0,//v0
            -1f, 1f, 0,//v1
            -1f, -1f, 0,//v2
            1f, -1f, 0//v3
    };
    static final int[] indices = new int[]{
            0, 1, 3,//top left triangle (v0, v1, v3)
            3, 1, 2//bottom right triangle (v3, v1, v2)
    };

    private static Window window;
    private static Time gameTime;

    public static void main(String[] args) {
        gameLoop().run();
    }

    private static Runnable gameLoop() {

        return () -> {
            window = new Window(300, 300 * 16 / 9);
            window.setApplicationName( "Uniform Rounded Corners Test");
            window.setDefaultBackground(Color.YELLOW);
            window.show();
            gameTime = Time.getInstance();

            String VERTEX_FILE_LOCATION = "src/test/resources/shaders/ComponentShader.vert";
            String FRAGMENT_FILE_LOCATION = "src/test/resources/shaders/ComponentShader.frag";
            ShaderProgram roundedCornersShaderProgram = new ShaderProgram();
            Shader v = Shader.loadShader(Shader.VERTEX, VERTEX_FILE_LOCATION);
            Shader f = Shader.loadShader(Shader.FRAGMENT, FRAGMENT_FILE_LOCATION);
            roundedCornersShaderProgram.attachShaders(v, f);
            roundedCornersShaderProgram.link();
            v.delete();
            f.delete();

            roundedCornersShaderProgram.getUniform("radiusPixels").asUniformFloat().update(50);
            setUpWindowBuffer(roundedCornersShaderProgram, window);

            StaticEntity r1 = getEntity(roundedCornersShaderProgram);
            r1.scale(0.5f, 0.5f, 0);
            r1.rotate(0, 0, 45);
            StaticRenderer flatRenderer = new StaticRenderer(roundedCornersShaderProgram);
            flatRenderer.addMesh(r1);

            while (window.closeNotRequested()) {
                r1.rotate(0, 0, 1);
                gameTime.updateTime();
                window.update();
                window.prepareForRender();
                flatRenderer.render();
                window.transferBuffersAfterRender();
            }

            flatRenderer.cleanUp();
            window.terminate();
        };
    }

    private static void setUpWindowBuffer(ShaderProgram shader, Window window) {
        int bufferBlockBinding = 0;
        String uniformBlockName = "WindowProperties";

        UniformBlockBuffer ubo = new UniformBlockBuffer();
        ubo.blockBind(bufferBlockBinding);
        shader.bindUniformBlock(uniformBlockName, bufferBlockBinding);

        Bounds bounds = window.getBounds();
        Std140Compatible vec2 = new std140ColumnVector(bounds.getWidth(), bounds.getHeight());
        ubo.updateBuffer(Std140Compatible.putAllInBuffer(vec2));
    }

    public static StaticEntity getEntity(ShaderProgram roundedCornersShaderProgram) {
        GPUMesh mesh = new GPUMesh(3);
        mesh.setPositions(positions, indices);
        StaticEntity r1 = new StaticEntity(mesh);
        r1.setTransformationUniform(roundedCornersShaderProgram.getUniform("modelMatrix"));
        return r1;
    }

}

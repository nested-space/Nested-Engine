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

import com.edenrump.graphic.entities.StaticEntity;
import com.edenrump.math.geom.PerspectiveProjection;
import com.edenrump.gpu.objects.Uniform;
import com.edenrump.graphic.mesh.GPUMesh;
import com.edenrump.graphic.render.StaticRenderer;
import com.edenrump.graphic.shaders.Shader;
import com.edenrump.graphic.shaders.ShaderProgram;
import com.edenrump.graphic.time.Time;
import com.edenrump.graphic.display.global.Window;
import org.lwjgl.BufferUtils;

import java.awt.*;
import java.nio.FloatBuffer;

public class OrthogonalProjectionTest {

    private static Window window;
    private static Time gameTime;

    public static void main(String[] args) {
        gameLoop().run();
    }

    private static Runnable gameLoop() {

        return () -> {
            window = new Window(300, 300);
            window.setApplicationName("Projection Test");
            window.setDefaultBackground(Color.YELLOW);
            window.show();
            gameTime = Time.getInstance();

            String VERTEX_FILE_LOCATION = "src/test/resources/shaders/ProjectionTestShader.vert";
            String FRAGMENT_FILE_LOCATION = "src/test/resources/shaders/ProjectionTestShader.frag";
            ShaderProgram shaderProgram = new ShaderProgram();
            Shader v = Shader.loadShader(Shader.VERTEX, VERTEX_FILE_LOCATION);
            Shader f = Shader.loadShader(Shader.FRAGMENT, FRAGMENT_FILE_LOCATION);
            shaderProgram.attachShaders(v, f);
            shaderProgram.link();
            v.delete();
            f.delete();

            PerspectiveProjection projection = PerspectiveProjection.defineByFieldOfView(70, (float) 16 / 9, 0.1f, 1000);
            Uniform uf = shaderProgram.getUniform("projectionMatrix");
            FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
            projection.getProjectionMatrix().storeMatrixInBuffer(buffer);
            buffer.flip();
            uf.asUniformMatrix().update_4x4(buffer);

            StaticEntity r1 = getEntity(shaderProgram);
            r1.scale(0.5f, 0.5f, -2);
            StaticRenderer flatRenderer = new StaticRenderer(shaderProgram);
            flatRenderer.addMesh(r1);

            while (window.closeNotRequested()) {
                r1.rotate(0, 0, 3);
                r1.translate(0, 0, -0.03f);
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

    public static StaticEntity getEntity(ShaderProgram roundedCornersShaderProgram) {
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
        StaticEntity r1 = new StaticEntity(mesh);
        r1.setTransformationUniform(roundedCornersShaderProgram.getUniform("modelMatrix"));
        return r1;
    }

}

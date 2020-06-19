package com.edenrump.gizmos;

import com.edenrump.graphic.entities.StaticEntity;
import com.edenrump.graphic.data.Std140Compatible;
import com.edenrump.graphic.data.std140ColumnVector;
import com.edenrump.graphic.mesh.GPUMesh;
import com.edenrump.graphic.gpu.UniformBlockBuffer;
import com.edenrump.graphic.render.StaticRenderer;
import com.edenrump.graphic.shaders.Shader;
import com.edenrump.graphic.shaders.ShaderProgram;
import com.edenrump.graphic.time.Time;
import com.edenrump.graphic.viewport.Screenshot;
import com.edenrump.graphic.viewport.Window;

import java.awt.*;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL20C.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20C.GL_VERTEX_SHADER;

public class WindowScreenShotTest {

    static float[] positions = new float[]{
            1f, 1f, 0,//v0
            -1f, 1f, 0,//v1
            -1f, -1f, 0,//v2
            1f, -1f, 0,//v3
    };
    static int[] indices = new int[]{
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
            window = new Window(0.3, 0.3 * 16 / 9, "Attribute Test", Color.YELLOW);
            window.create(false);
            window.show();
            gameTime = Time.getInstance();

            String VERTEX_FILE_LOCATION = "src/test/resources/shaders/roundedCornersTestShader.vert";
            String FRAGMENT_FILE_LOCATION = "src/test/resources/shaders/roundedCornersTestShader.frag";
            ShaderProgram shaderProgram = new ShaderProgram();
            Shader v = Shader.loadShader(GL_VERTEX_SHADER, VERTEX_FILE_LOCATION);
            Shader f = Shader.loadShader(GL_FRAGMENT_SHADER, FRAGMENT_FILE_LOCATION);
            shaderProgram.attachShaders(v, f);
            shaderProgram.link();
            v.delete();
            f.delete();

            shaderProgram.getUniform("radiusPixels").asUniformFloat().update(15);

            System.out.println(shaderProgram.getUniform("scale").getLocation());

            setUpWindowBuffer(shaderProgram, window);

            StaticEntity r1 = getEntity(shaderProgram);
            r1.scale(0.5f, 0.5f, 1);
            StaticRenderer flatRenderer = new StaticRenderer(shaderProgram);
            flatRenderer.addMesh(r1);

            shaderProgram.getUniform("scale").asUniformFloat().update2values(r1.getScale()[0], r1.getScale()[1]);

            while (!window.isCloseRequested()) {
                r1.rotate(0, 0, 0);
                r1.scale(1.001f, 1, 1);
                shaderProgram.getUniform("scale").asUniformFloat().update2values(r1.getScale()[0], r1.getScale()[1]);

                gameTime.updateTime();
                window.update();
                window.prepareForRender();
                flatRenderer.render();
                window.transferBuffersAfterRender();

                int width = window.getWidth();
                int height = window.getHeight();
                int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
                ByteBuffer screenData = Screenshot.getWindowPixelData(width, height, bpp);
                Screenshot.saveScreenDataToFile(
                        screenData,
                        window.getWidth(),
                        window.getHeight(),
                        bpp,
                        String.valueOf(gameTime.getCurrentTimeMillis()));
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

        Std140Compatible vec2 = new std140ColumnVector(window.getWidth(), window.getHeight());
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

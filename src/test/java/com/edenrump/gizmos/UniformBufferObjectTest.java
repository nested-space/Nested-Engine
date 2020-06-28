package com.edenrump.gizmos;

import com.edenrump.graphic.data.Std140Compatible;
import com.edenrump.graphic.data.std140ColumnVector;
import com.edenrump.graphic.data.std140SquareMatrix;
import com.edenrump.graphic.entities.StaticEntity;
import com.edenrump.graphic.gpu.UniformBlockBuffer;
import com.edenrump.graphic.mesh.GPUMesh;
import com.edenrump.graphic.render.StaticRenderer;
import com.edenrump.graphic.shaders.Shader;
import com.edenrump.graphic.shaders.ShaderProgram;
import com.edenrump.graphic.time.Time;
import com.edenrump.graphic.viewport.display.Window;

import java.awt.*;

public class UniformBufferObjectTest {

    //files required for this shader
    final static String VERTEX_FILE_LOCATION = "src/test/resources/shaders/UniformBufferObjectTestShader.vert";
    final static String FRAGMENT_FILE_LOCATION = "src/test/resources/shaders/UniformBufferObjectTestShader.frag";
    static final float[] positions = {
            -0.5f, 0.5f, 0,//v0
            -0.5f, -0.5f, 0,//v1
            0.5f, -0.5f, 0,//v2
            0.5f, 0.5f, 0,//v3
    };
    static final int[] indices = {
            0, 1, 3,//top left triangle (v0, v1, v3)
            3, 1, 2//bottom right triangle (v3, v1, v2)
    };
    static GPUMesh rectangle;
    private static Window window;
    private static Time gameTime;

    public static void main(String[] args) {
        gameLoop().run();
    }

    private static Runnable gameLoop() {

        return () -> {
            window = new Window(300, 300);
            window.setApplicationName( "Uniform Buffer Object Test");
            window.setDefaultBackground(Color.YELLOW);
            window.show();
            gameTime = Time.getInstance();

            ShaderProgram uniformBufferTestShader = new ShaderProgram();
            Shader v = Shader.loadShader(Shader.VERTEX, VERTEX_FILE_LOCATION);
            Shader f = Shader.loadShader(Shader.FRAGMENT, FRAGMENT_FILE_LOCATION);
            uniformBufferTestShader.attachShaders(v, f);
            uniformBufferTestShader.link();
            v.delete();
            f.delete();

            int bufferBlockBinding = 0;
            String uniformBlockName = "TestBlock";

            UniformBlockBuffer ubo = new UniformBlockBuffer();
            ubo.blockBind(bufferBlockBinding);
            uniformBufferTestShader.bindUniformBlock(uniformBlockName, bufferBlockBinding);

            Std140Compatible mat4Padding = new std140SquareMatrix(4);
            Std140Compatible vec3ColorY = new std140ColumnVector(0.7f, 1f, 0.4f);
            ubo.updateBuffer(Std140Compatible.putAllInBuffer(mat4Padding, vec3ColorY));

            rectangle = new GPUMesh(3);
            rectangle.setPositions(positions, indices);

            StaticEntity rectEntity = new StaticEntity(rectangle);

            StaticRenderer flatRenderer = new StaticRenderer(uniformBufferTestShader);
            flatRenderer.addMesh(rectEntity);

            while (window.closeNotRequested()) {
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

}

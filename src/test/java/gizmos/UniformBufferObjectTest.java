package gizmos;

import com.edenrump.graphic.math.Std140Compatible;
import com.edenrump.graphic.math.glColumnVector;
import com.edenrump.graphic.math.glSquareMatrix;
import com.edenrump.graphic.mesh.Mesh;
import com.edenrump.graphic.mesh.MeshUtils;
import com.edenrump.graphic.render.FlatRenderer;
import com.edenrump.graphic.shaders.Shader;
import com.edenrump.graphic.shaders.ShaderProgram;
import com.edenrump.graphic.time.Time;
import com.edenrump.graphic.viewport.Window;
import org.lwjgl.BufferUtils;

import java.awt.*;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20C.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20C.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL31.*;

public class UniformBufferObjectTest {

    static float[] positions = {
            -0.5f, 0.5f,//v0
            -0.5f, -0.5f,//v1
            0.5f, -0.5f,//v2
            0.5f, 0.5f,//v3
    };
    static int[] indices = {
            0, 1, 3,//top left triangle (v0, v1, v3)
            3, 1, 2//bottom right triangle (v3, v1, v2)
    };

    static Mesh GUI;
    private static Window window;
    private static Time gameTime;

    public static void main(String[] args) {
        gameLoop().run();
    }

    private static Runnable gameLoop() {

        return () -> {
            window = new Window(0.5, 0.5, "Attribute Test", Color.YELLOW);
            window.create(false);
            window.show();
            gameTime = Time.getInstance();

            //files required for this shader
            final String VERTEX_FILE_LOCATION = "src/test/resources/shaders/UniformBufferObjectTestShader.vert";
            final String FRAGMENT_FILE_LOCATION = "src/test/resources/shaders/UniformBufferObjectTestShader.frag";
            ShaderProgram uniformBufferTestShader;
            Shader v = Shader.loadShader(GL_VERTEX_SHADER, VERTEX_FILE_LOCATION);
            Shader f = Shader.loadShader(GL_FRAGMENT_SHADER, FRAGMENT_FILE_LOCATION);
            uniformBufferTestShader = new ShaderProgram();
            uniformBufferTestShader.attachShaders(v, f);
            uniformBufferTestShader.link();
            v.delete();
            f.delete();

            //create color in memory
            int block2UBOID = glGenBuffers();
            int bufferBlockBinding = 0;
            glBindBuffer(GL_UNIFORM_BUFFER, block2UBOID);
            glBindBufferBase(GL_UNIFORM_BUFFER, bufferBlockBinding, block2UBOID);
            glBufferData(GL_UNIFORM_BUFFER, 80, GL_DYNAMIC_DRAW);
            glBindBuffer(GL_UNIFORM_BUFFER, 0);

            String uniformBlockName = "TestBlock";
            glUseProgram(uniformBufferTestShader.getId());
            glUniformBlockBinding(
                    uniformBufferTestShader.getId(),
                    glGetUniformBlockIndex(uniformBufferTestShader.getId(), uniformBlockName),
                    bufferBlockBinding);
            glUseProgram(0);

            Std140Compatible mat4Padding = new glSquareMatrix(4);
            Std140Compatible vec3ColorY = new glColumnVector(0.7f, 0.1f, 0.1f);
            FloatBuffer buffer2 = BufferUtils.createFloatBuffer(80);
            mat4Padding.storeStd140DataInBuffer(buffer2);
            vec3ColorY.storeStd140DataInBuffer(buffer2);
            buffer2.flip();
            glBindBuffer(GL_UNIFORM_BUFFER, block2UBOID);
            glBufferSubData(GL_UNIFORM_BUFFER, 0, buffer2);
            glBindBuffer(GL_UNIFORM_BUFFER, 0);

            GUI = MeshUtils.loadMesh2D(positions, indices);
            FlatRenderer flatRenderer = new FlatRenderer(uniformBufferTestShader);
            flatRenderer.addMesh(GUI);

            while (!window.isCloseRequested()) {
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

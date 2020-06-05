package gizmos;

import com.edenrump.graphic.entities.GUI_StaticEntity;
import com.edenrump.graphic.math.Std140Compatible;
import com.edenrump.graphic.math.std140ColumnVector;
import com.edenrump.graphic.math.std140SquareMatrix;
import com.edenrump.graphic.components.Mesh;
import com.edenrump.graphic.openGL_gpu.UniformBlockBuffer;
import com.edenrump.graphic.render.GUI_StaticRenderer;
import com.edenrump.graphic.shaders.Shader;
import com.edenrump.graphic.shaders.ShaderProgram;
import com.edenrump.graphic.time.Time;
import com.edenrump.graphic.viewport.Window;

import java.awt.*;

import static org.lwjgl.opengl.GL20C.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20C.GL_VERTEX_SHADER;

public class UniformBufferObjectTest {

    //files required for this shader
    final static String VERTEX_FILE_LOCATION = "src/test/resources/shaders/UniformBufferObjectTestShader.vert";
    final static String FRAGMENT_FILE_LOCATION = "src/test/resources/shaders/UniformBufferObjectTestShader.frag";
    static float[] positions = {
            -0.5f, 0.5f, 0,//v0
            -0.5f, -0.5f, 0,//v1
            0.5f, -0.5f, 0,//v2
            0.5f, 0.5f, 0,//v3
    };
    static int[] indices = {
            0, 1, 3,//top left triangle (v0, v1, v3)
            3, 1, 2//bottom right triangle (v3, v1, v2)
    };
    static Mesh rectangle;
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

            ShaderProgram uniformBufferTestShader = new ShaderProgram();
            Shader v = Shader.loadShader(GL_VERTEX_SHADER, VERTEX_FILE_LOCATION);
            Shader f = Shader.loadShader(GL_FRAGMENT_SHADER, FRAGMENT_FILE_LOCATION);
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

            rectangle = new Mesh();
            rectangle.setPositions(positions, indices);

            GUI_StaticEntity rectEntity = new GUI_StaticEntity(rectangle);

            GUI_StaticRenderer flatRenderer = new GUI_StaticRenderer(uniformBufferTestShader);
            flatRenderer.addMesh(rectEntity);

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

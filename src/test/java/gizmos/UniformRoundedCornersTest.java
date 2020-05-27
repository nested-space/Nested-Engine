package gizmos;

import com.edenrump.graphic.entities.GUI_StaticEntity;
import com.edenrump.graphic.math.Std140Compatible;
import com.edenrump.graphic.math.std140ColumnVector;
import com.edenrump.graphic.mesh.GUI_StaticMesh;
import com.edenrump.graphic.openGL_gpu.UniformBlockBuffer;
import com.edenrump.graphic.render.GUI_StaticRenderer;
import com.edenrump.graphic.shaders.Shader;
import com.edenrump.graphic.shaders.ShaderProgram;
import com.edenrump.graphic.time.Time;
import com.edenrump.graphic.viewport.Window;

import java.awt.*;

import static org.lwjgl.opengl.GL20C.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20C.GL_VERTEX_SHADER;

public class UniformRoundedCornersTest {

    static float[] positions = new float[]{
            1f, 1f, 0,//v0
            -1f, 1f, 0,//v1
            -1f, -1f, 0,//v2
            1f, -1f, 0//v3
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
            window = new Window(0.5, 0.5 * 16 / 9, "Attribute Test", Color.YELLOW);
            window.create(false);
            window.show();
            gameTime = Time.getInstance();

            String VERTEX_FILE_LOCATION = "src/test/resources/shaders/roundedCornersTestShader.vert";
            String FRAGMENT_FILE_LOCATION = "src/test/resources/shaders/roundedCornersTestShader.frag";
            ShaderProgram roundedCornersShaderProgram = new ShaderProgram();
            Shader v = Shader.loadShader(GL_VERTEX_SHADER, VERTEX_FILE_LOCATION);
            Shader f = Shader.loadShader(GL_FRAGMENT_SHADER, FRAGMENT_FILE_LOCATION);
            roundedCornersShaderProgram.attachShaders(v, f);
            roundedCornersShaderProgram.link();
            v.delete();
            f.delete();

            roundedCornersShaderProgram.getUniform("radiusPixels").asUniformFloat().update(50);
            setUpWindowBuffer(roundedCornersShaderProgram, window);

            GUI_StaticEntity r1 = getEntity(roundedCornersShaderProgram);
            r1.scale(0.5f, 0.5f, 0);
            r1.rotate(0, 0, 45);
            GUI_StaticRenderer flatRenderer = new GUI_StaticRenderer(roundedCornersShaderProgram);
            flatRenderer.addMesh(r1);

            while (!window.isCloseRequested()) {
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

        Std140Compatible vec2 = new std140ColumnVector(window.getWidth(), window.getHeight());
        ubo.updateBuffer(Std140Compatible.putAllInBuffer(vec2));
    }

    public static GUI_StaticEntity getEntity(ShaderProgram roundedCornersShaderProgram) {
        GUI_StaticMesh mesh = new GUI_StaticMesh();
        mesh.setPositions(positions, indices);
        GUI_StaticEntity r1 = new GUI_StaticEntity(mesh);
        r1.setTransformationUniform(roundedCornersShaderProgram.getUniform("modelMatrix"));
        return r1;
    }

}

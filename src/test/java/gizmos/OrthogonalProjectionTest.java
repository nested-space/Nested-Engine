package gizmos;

import com.edenrump.graphic.entities.GUI_StaticEntity;
import com.edenrump.graphic.geom.PerspectiveProjection;
import com.edenrump.graphic.gpu.GPUMesh;
import com.edenrump.graphic.gpu.Uniform;
import com.edenrump.graphic.render.StaticRenderer;
import com.edenrump.graphic.shaders.Shader;
import com.edenrump.graphic.shaders.ShaderProgram;
import com.edenrump.graphic.time.Time;
import com.edenrump.graphic.viewport.Window;
import org.lwjgl.BufferUtils;

import java.awt.*;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20C.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20C.GL_VERTEX_SHADER;

public class OrthogonalProjectionTest {

    private static Window window;
    private static Time gameTime;

    public static void main(String[] args) {
        gameLoop().run();
    }

    private static Runnable gameLoop() {

        return () -> {
            window = new Window(0.3, 0.3, "Attribute Test", Color.YELLOW);
            window.create(false);
            window.show();
            gameTime = Time.getInstance();

            String VERTEX_FILE_LOCATION = "src/test/resources/shaders/ProjectionTestShader.vert";
            String FRAGMENT_FILE_LOCATION = "src/test/resources/shaders/ProjectionTestShader.frag";
            ShaderProgram shaderProgram = new ShaderProgram();
            Shader v = Shader.loadShader(GL_VERTEX_SHADER, VERTEX_FILE_LOCATION);
            Shader f = Shader.loadShader(GL_FRAGMENT_SHADER, FRAGMENT_FILE_LOCATION);
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

            GUI_StaticEntity r1 = getEntity(shaderProgram);
            r1.scale(0.5f, 0.5f, -2);
            StaticRenderer flatRenderer = new StaticRenderer(shaderProgram);
            flatRenderer.addMesh(r1);

            while (!window.isCloseRequested()) {
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

    public static GUI_StaticEntity getEntity(ShaderProgram roundedCornersShaderProgram) {
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

        GPUMesh mesh = new GPUMesh();
        mesh.setPositions(positions, indices);
        GUI_StaticEntity r1 = new GUI_StaticEntity(mesh);
        r1.setTransformationUniform(roundedCornersShaderProgram.getUniform("modelMatrix"));
        return r1;
    }

}

package gizmos;

import com.edenrump.graphic.entities.Flat_StaticEntity;
import com.edenrump.graphic.math.Std140Compatible;
import com.edenrump.graphic.math.glColumnVector;
import com.edenrump.graphic.math.glSquareMatrix;
import com.edenrump.graphic.mesh.Flat_StaticMesh;
import com.edenrump.graphic.openGL_gpu.Uniform;
import com.edenrump.graphic.openGL_gpu.UniformBlockBuffer;
import com.edenrump.graphic.render.Flat_StaticRenderer;
import com.edenrump.graphic.shaders.Shader;
import com.edenrump.graphic.shaders.ShaderProgram;
import com.edenrump.graphic.time.Time;
import com.edenrump.graphic.viewport.Window;

import java.awt.*;

import static org.lwjgl.opengl.GL20C.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20C.GL_VERTEX_SHADER;

public class UniformRoundedCornersTest {

    static float[] positions = new float[]{
            1f, 1f,//v0
            -1f, 1f,//v1
            -1f, -1f,//v2
            1f, -1f,//v3
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
            window = new Window(0.5, 0.5, "Attribute Test", Color.YELLOW);
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

            roundedCornersShaderProgram.getUniform("radiusPixels").asUniformFloat().update(15);

            Flat_StaticEntity r1 = getEntity(roundedCornersShaderProgram);
            r1.scale(0.5f, 0.5f, 1);
            r1.translate(0.5f, 0.5f, 0);
            Flat_StaticRenderer flatRenderer = new Flat_StaticRenderer(roundedCornersShaderProgram);
            flatRenderer.addMesh(r1);

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

    private static void setUpWindowBuffer(ShaderProgram shader, Window window) {
        int bufferBlockBinding = 0;
        String uniformBlockName = "WindowProperties";

        UniformBlockBuffer ubo = new UniformBlockBuffer();
        ubo.blockBind(bufferBlockBinding);
        shader.bindUniformBlock(uniformBlockName, bufferBlockBinding);

        Std140Compatible vec2 = new glColumnVector(window.getWidth(), window.getHeight());
        ubo.updateBuffer(Std140Compatible.putAllInBuffer(vec2));
    }

    public static Flat_StaticEntity getEntity(ShaderProgram roundedCornersShaderProgram){
        Flat_StaticMesh mesh = new Flat_StaticMesh();
        mesh.setPositions(positions, indices);
        Flat_StaticEntity r1 = new Flat_StaticEntity(mesh);
        r1.setTransformationUniform(roundedCornersShaderProgram.getUniform("transformationMatrix"));
        return r1;
    }

}

package gizmos;

import com.edenrump.graphic.entities.GUI_StaticEntity;
import com.edenrump.graphic.gpu.GPUMesh;
import com.edenrump.graphic.gpu.Uniform;
import com.edenrump.graphic.render.StaticRenderer;
import com.edenrump.graphic.shaders.Shader;
import com.edenrump.graphic.shaders.ShaderProgram;
import com.edenrump.graphic.time.Time;
import com.edenrump.graphic.viewport.Window;

import java.awt.*;

import static org.lwjgl.opengl.GL20C.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20C.GL_VERTEX_SHADER;

public class UniformVariableTest {

    //files required for this shader
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

            String VERTEX_FILE_LOCATION = "src/test/resources/shaders/UniformColorTestShader.vert";
            String FRAGMENT_FILE_LOCATION = "src/test/resources/shaders/UniformColorTestShader.frag";

            ShaderProgram uniformColorTestShader = new ShaderProgram();
            Shader v = Shader.loadShader(GL_VERTEX_SHADER, VERTEX_FILE_LOCATION);
            Shader f = Shader.loadShader(GL_FRAGMENT_SHADER, FRAGMENT_FILE_LOCATION);
            uniformColorTestShader.attachShaders(v, f);
            uniformColorTestShader.link();
            v.delete();
            f.delete();

            Uniform uf = uniformColorTestShader.getUniform("color");
            uf.asUniformFloat().update3values(1, 0.5f, 0);

            GPUMesh mesh1 = new GPUMesh();
            mesh1.setPositions(new float[]{
                            0.75f, 0.6f, 0,//v0
                            -0.75f, 0.6f, 0,//v1
                            -0.75f, 0.1f, 0,//v2
                            0.75f, 0.1f, 0,//v3
                    },
                    new int[]{
                            0, 1, 3,//top left triangle (v0, v1, v3)
                            3, 1, 2//bottom right triangle (v3, v1, v2)
                    });

            GPUMesh mesh2 = new GPUMesh();
            mesh2.setPositions(
                    new float[]{
                            0.75f, 0f, 0f,//v0
                            -0.75f, 0f, 0f,//v1
                            -0.75f, -0.5f, 0f,//v2
                            0.75f, -0.5f, 0f//v3
                    },
                    new int[]{
                            0, 1, 3,//top left triangle (v0, v1, v3)
                            3, 1, 2//bottom right triangle (v3, v1, v2)
                    });

            GUI_StaticEntity r1 = new GUI_StaticEntity(mesh1);
            GUI_StaticEntity r2 = new GUI_StaticEntity(mesh2);
            StaticRenderer flatRenderer = new StaticRenderer(uniformColorTestShader);
            flatRenderer.addMesh(r1);
            flatRenderer.addMesh(r2);

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

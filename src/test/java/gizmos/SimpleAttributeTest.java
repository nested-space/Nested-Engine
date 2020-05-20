package gizmos;

import com.edenrump.graphic.openGL_gpu.VertexBufferObject;
import com.edenrump.graphic.shaders.ShaderProgram;
import com.edenrump.math.util.DataUtils;
import com.edenrump.graphic.mesh.Mesh;
import com.edenrump.graphic.render.FlatRenderer;
import com.edenrump.graphic.time.Time;
import com.edenrump.graphic.mesh.MeshUtils;
import com.edenrump.graphic.viewport.Window;

import java.awt.*;

import static com.edenrump.graphic.openGL_gpu.Attribute.POSITIONS_ATTRIB_NAME;
import static org.lwjgl.opengl.GL20.*;

public class SimpleAttributeTest {

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

            GUI = MeshUtils.loadTexturedMesh2D(positions, indices, "res/textures/256_256_4-bit-noise.png");

            FlatRenderer flatRenderer = new FlatRenderer(ShaderProgram.simpleTextureShaderProgram());
            flatRenderer.addMesh(GUI);

            while (!window.isCloseRequested()) {
                gameTime.updateTime();
                window.update();
                updateGUI();

                window.prepareForRender();

                flatRenderer.render();

                window.transferBuffersAfterRender();
            }

            flatRenderer.cleanUp();
            window.terminate();
        };
    }

    private static void updateGUI(){
        float heightFraction = 0.2f;
        float widthFraction = heightFraction / 2 * window.getAspectRatio();
        float[] positions = {
                -widthFraction, heightFraction,//v0
                -widthFraction, -heightFraction,//v1
                widthFraction, -heightFraction,//v2
                widthFraction, heightFraction,//v3
        };

        int positionVBO = GUI.getAttribute(POSITIONS_ATTRIB_NAME).getVBOId();
        VertexBufferObject.bind(positionVBO, GL_ARRAY_BUFFER);
        VertexBufferObject.uploadData(GL_ARRAY_BUFFER, DataUtils.storeDataInBuffer(positions), GL_STATIC_DRAW);
    }
}

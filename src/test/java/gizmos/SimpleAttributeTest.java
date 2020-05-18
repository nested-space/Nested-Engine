package gizmos;

import com.edenrump.graphic.data.VertexBufferObject;
import com.edenrump.math.util.DataUtils;
import com.edenrump.graphic.mesh.Mesh;
import com.edenrump.graphic.render.renderers.Renderer;
import com.edenrump.graphic.shaders.DefaultShaderPrograms;
import com.edenrump.graphic.time.Time;
import com.edenrump.graphic.util.VAOEasyLoader;
import com.edenrump.graphic.viewport.Window;

import java.awt.*;

import static com.edenrump.graphic.mesh.MeshVariables.POSITIONS_ATTRIB_NAME;
import static org.lwjgl.opengl.GL20.*;

public class SimpleAttributeTest {

    static float[] positions = {
            -0.5f, 0.5f, 0f,//v0
            -0.5f, -0.5f, 0f,//v1
            0.5f, -0.5f, 0f,//v2
            0.5f, 0.5f, 0f,//v3
    };
    static int[] indices = {
            0, 1, 3,//top left triangle (v0, v1, v3)
            3, 1, 2//bottom right triangle (v3, v1, v2)
    };
    static float[] textureCoords = {
            0, 0,    //V0
            0, 1,    //V1
            1, 1,    //V2
            1, 0     //V3
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

            GUI = VAOEasyLoader.loadTexturedMesh(positions, indices, textureCoords, "res/textures/256_256_4-bit-noise.png");

            Renderer renderer = new Renderer(DefaultShaderPrograms.getDefaultTextureShader());
            renderer.addMesh(GUI);

            while (!window.isCloseRequested()) {
                gameTime.updateTime();
                window.update();
                updateGUI();

                window.prepareForRender();

                renderer.render();

                window.transferBuffersAfterRender();
            }

            window.terminate();
        };
    }

    static Mesh GUI;

    private static void updateGUI(){
        float heightFraction = 0.2f;
        float widthFraction = heightFraction / 2 * window.getAspectRatio();
        float[] positions = {
                -widthFraction, heightFraction, 0f,//v0
                -widthFraction, -heightFraction, 0f,//v1
                widthFraction, -heightFraction, 0f,//v2
                widthFraction, heightFraction, 0f,//v3
        };

        VertexBufferObject positionVBO = GUI.getAttachedBuffer(POSITIONS_ATTRIB_NAME);
        positionVBO.bind(GL_ARRAY_BUFFER);
        positionVBO.uploadData(GL_ARRAY_BUFFER, DataUtils.storeDataInBuffer(positions), GL_STATIC_DRAW);
    }
}

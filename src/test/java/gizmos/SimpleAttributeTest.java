package gizmos;

import com.edenrump.graphic.mesh.Dynamic_TexturedMesh3D;
import com.edenrump.graphic.openGL_gpu.Texture;
import com.edenrump.graphic.render.FlatRenderer;
import com.edenrump.graphic.shaders.ShaderProgram;
import com.edenrump.graphic.time.Time;
import com.edenrump.graphic.viewport.Window;

import java.awt.*;

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

    static Dynamic_TexturedMesh3D rectangle;
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

            String textureFile = "src/test/resources/textures/256_256_4-bit-noise.png";
            rectangle = new Dynamic_TexturedMesh3D();
            rectangle.setPositions(positions, indices);
            rectangle.setTexture(new float[]{0, 0, 0, 1, 1, 1, 1, 0}, Texture.loadTexture(textureFile));

            FlatRenderer flatRenderer = new FlatRenderer(ShaderProgram.simpleTextureShaderProgram());
            flatRenderer.addMesh(rectangle);

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

    private static void updateGUI() {
        float heightFraction = 0.2f;
        float widthFraction = heightFraction / 2 * window.getAspectRatio();
        float[] positions = {
                -widthFraction, heightFraction,//v0
                -widthFraction, -heightFraction,//v1
                widthFraction, -heightFraction,//v2
                widthFraction, heightFraction,//v3
        };

        rectangle.updatePositions(positions);
    }
}

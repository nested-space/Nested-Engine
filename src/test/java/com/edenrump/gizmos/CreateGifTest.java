package com.edenrump.gizmos;

import com.edenrump.graphic.entities.StaticEntity;
import com.edenrump.graphic.geom.PerspectiveProjection;
import com.edenrump.graphic.gpu.Uniform;
import com.edenrump.graphic.mesh.CPUMesh;
import com.edenrump.graphic.mesh.ConstructConverter;
import com.edenrump.graphic.mesh.GPUMesh;
import com.edenrump.graphic.render.StaticRenderer;
import com.edenrump.graphic.shaders.Shader;
import com.edenrump.graphic.shaders.ShaderProgram;
import com.edenrump.graphic.time.Time;
import com.edenrump.graphic.viewport.GifSequenceWriter;
import com.edenrump.graphic.viewport.Screenshot;
import com.edenrump.graphic.viewport.Window;
import com.edenrump.math.shape.mesh.GeometricConstruct;
import com.edenrump.math.shape.mesh.ShadingType;
import com.edenrump.math.shape.solids.Icosahedron;
import org.lwjgl.BufferUtils;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL30C.GL_FRAMEBUFFER_SRGB;

public class CreateGifTest {

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

            String VERTEX_FILE_LOCATION = "src/test/resources/shaders/ProjectionTestShader.vert";
            String FRAGMENT_FILE_LOCATION = "src/test/resources/shaders/ProjectionTestShader.frag";

            ShaderProgram shaderProgram = new ShaderProgram();
            Shader v = Shader.loadShader(Shader.VERTEX, VERTEX_FILE_LOCATION);
            Shader f = Shader.loadShader(Shader.FRAGMENT, FRAGMENT_FILE_LOCATION);
            shaderProgram.attachShaders(v, f);
            shaderProgram.link();
            v.delete();
            f.delete();

            glEnable(GL_CULL_FACE);
            glEnable(GL_DEPTH_TEST);
            glEnable(GL_FRAMEBUFFER_SRGB);

            StaticEntity rectEntity = getEntity(shaderProgram);

            PerspectiveProjection projection = PerspectiveProjection.defineByFieldOfView(70, (float) 16 / 9, 0.1f, 1000);
            Uniform uf = shaderProgram.getUniform("projectionMatrix");
            FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
            projection.getProjectionMatrix().storeMatrixInBuffer(buffer);
            buffer.flip();
            uf.asUniformMatrix().update_4x4(buffer);

            StaticRenderer flatRenderer = new StaticRenderer(shaderProgram);
            flatRenderer.addMesh(rectEntity);
            rectEntity.translate(0, 0, -3f);
            rectEntity.rotate(90, 0, 0);

            ImageOutputStream output = null;
            GifSequenceWriter writer = null;
            try {
                File file = new File("src/test/resources/img/example.gif");
                System.out.println(file.getAbsolutePath());
                output = new FileImageOutputStream(file);
                writer = new GifSequenceWriter(output, TYPE_INT_RGB, 1000 / 30, true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int count = 0;
            while (window.closeNotRequested()) {
                rectEntity.rotate(1f, 2f, 0);
                gameTime.updateTime();
                window.update();
                window.prepareForRender();
                flatRenderer.render();
                window.transferBuffersAfterRender();

                int width = window.getWidth();
                int height = window.getHeight();
                int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
                if (count < 360) {
                    ByteBuffer screenData = Screenshot.getWindowPixelData(width, height, bpp);
                    BufferedImage frame = Screenshot.convertToBufferedImage(
                            screenData,
                            window.getWidth(),
                            window.getHeight(),
                            bpp);
                    try {
                        writer.writeToSequence(frame);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    count++;
                }
            }

            try {
                writer.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            flatRenderer.cleanUp();
            window.terminate();
        };
    }

    public static StaticEntity getEntity(ShaderProgram roundedCornersShaderProgram) {
        Icosahedron solid = new Icosahedron(0.7f);
        solid.setShadingType(ShadingType.FLAT);
        GeometricConstruct construct = solid.getMesh();
        CPUMesh mesh = ConstructConverter.convertConstructToMesh(construct);
        GPUMesh committed = mesh.commitToGPU();

        StaticEntity r1 = new StaticEntity(committed);
        r1.setTransformationUniform(roundedCornersShaderProgram.getUniform("modelMatrix"));
        return r1;
    }

}

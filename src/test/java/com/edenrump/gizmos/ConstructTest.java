package com.edenrump.gizmos;

import com.edenrump.graphic.data.Std140Compatible;
import com.edenrump.graphic.data.std140ColumnVector;
import com.edenrump.graphic.data.std140SquareMatrix;
import com.edenrump.graphic.entities.StaticEntity;
import com.edenrump.graphic.geom.PerspectiveProjection;
import com.edenrump.graphic.gpu.Uniform;
import com.edenrump.graphic.gpu.UniformBlockBuffer;
import com.edenrump.graphic.mesh.CPUMesh;
import com.edenrump.graphic.mesh.GPUMesh;
import com.edenrump.graphic.mesh.ConstructConverter;
import com.edenrump.graphic.render.StaticRenderer;
import com.edenrump.graphic.shaders.Shader;
import com.edenrump.graphic.shaders.ShaderProgram;
import com.edenrump.graphic.time.Time;
import com.edenrump.graphic.viewport.GifSequenceWriter;
import com.edenrump.graphic.viewport.Screenshot;
import com.edenrump.graphic.viewport.Window;
import com.edenrump.loaders.OBJFile;
import com.edenrump.math.shape.mesh.GeometricConstruct;
import com.edenrump.math.shape.mesh.ShadingType;
import com.edenrump.math.shape.solids.Icosahedron;
import com.edenrump.math.shape.textured.WrappedConstruct;
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
import static org.lwjgl.opengl.GL20C.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20C.GL_VERTEX_SHADER;

public class ConstructTest {

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
            Shader v = Shader.loadShader(GL_VERTEX_SHADER, VERTEX_FILE_LOCATION);
            Shader f = Shader.loadShader(GL_FRAGMENT_SHADER, FRAGMENT_FILE_LOCATION);
            shaderProgram.attachShaders(v, f);
            shaderProgram.link();
            v.delete();
            f.delete();

            glEnable(GL_CULL_FACE);
            glEnable(GL_DEPTH_TEST);

            int bufferBlockBinding = 0;
            String uniformBlockName = "TestBlock";

            Uniform colorUniform = shaderProgram.getUniform("color");
            colorUniform.asUniformFloat().update3values(1, 0.5f, 0);

            UniformBlockBuffer ubo = new UniformBlockBuffer();
            ubo.blockBind(bufferBlockBinding);
            shaderProgram.bindUniformBlock(uniformBlockName, bufferBlockBinding);

            Std140Compatible mat4Padding = new std140SquareMatrix(4);
            Std140Compatible vec3ColorY = new std140ColumnVector(0.7f, 1f, 0.4f);
            ubo.updateBuffer(Std140Compatible.putAllInBuffer(mat4Padding, vec3ColorY));

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
                file.createNewFile();
                output = new FileImageOutputStream(file);
                writer = new GifSequenceWriter(output, TYPE_INT_RGB, 1000 / 30, true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int count=0;
            while (!window.isCloseRequested()) {
                rectEntity.rotate(1f, 2f, 0);
                gameTime.updateTime();
                window.update();
                window.prepareForRender();
                flatRenderer.render();
                window.transferBuffersAfterRender();

                int width = window.getWidth();
                int height = window.getHeight();
                int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
                if(false && count < 90){
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

    private static void setUpWindowBuffer(ShaderProgram shader, Window window) {
        int bufferBlockBinding = 0;
        String uniformBlockName = "WindowProperties";

        UniformBlockBuffer ubo = new UniformBlockBuffer();
        ubo.blockBind(bufferBlockBinding);
        shader.bindUniformBlock(uniformBlockName, bufferBlockBinding);

        Std140Compatible vec2 = new std140ColumnVector(window.getWidth(), window.getHeight());
        ubo.updateBuffer(Std140Compatible.putAllInBuffer(vec2));
    }

}

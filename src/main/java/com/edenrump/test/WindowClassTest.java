package com.edenrump.test;

import com.edenrump.graphic.data.VertexArrayObject;
import com.edenrump.graphic.render.renderers.EntityRenderer;
import com.edenrump.graphic.shaders.StaticEntityShaderProgram;
import com.edenrump.graphic.time.Time;
import com.edenrump.graphic.viewport.Window;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WindowClassTest implements Runnable {

    private Thread gameThread;

    /**
     * Method to create thread for the main gameThread loop and start it running.
     */
    public WindowClassTest() {
        gameThread = new Thread(this, "gameThread");
        gameThread.start();
    }

    private Window mainWindow;

    private StaticEntityShaderProgram shader;

    @Override
    public void run() {
        mainWindow = new Window(0.5, 0.5, "New Game!", Color.YELLOW);
        mainWindow.create(false);
        mainWindow.show();

        //create world
        createWorld();

        //run game
        while (!mainWindow.isCloseRequested()) {
            update();
            render();
        }

        //end game
        close();
    }

    List<VertexArrayObject> VAOList = new ArrayList<>();

    private void createWorld() {

        //create Shader
        shader = new StaticEntityShaderProgram();

        //create world
        float[] vertices = {
                -0.5f, 0.5f, 0f,//v0
                -0.5f, -0.5f, 0f,//v1
                0.5f, -0.5f, 0f,//v2
                0.5f, 0.5f, 0f,//v3
        };

        int[] indices = {
                0, 1, 3,//top left triangle (v0, v1, v3)
                3, 1, 2//bottom right triangle (v3, v1, v2)
        };

        float[] textureCoords = {
                0,0,    //V0
                0,1,    //V1
                1,1,    //V2
                1,0     //V3
        };


        //TODO: create VAOs

    }

    /* ****************************************************************************************************************
     * Render functions
     * ****************************************************************************************************************/

    private Time gameTime = Time.getInstance();
    private EntityRenderer renderer = new EntityRenderer();

    private void update() {
        //TODO: test whether screen dimensions have changed. If yes, update screen dimensions.
        gameTime.updateTime();
        mainWindow.update();
    }

    private void render() {

        //start shader here

        renderer.prepare();
        for (VertexArrayObject vao : VAOList) {
            renderer.render();
        }

        //stop shader here

        mainWindow.transferBuffersAfterRender();
    }

    private void close() {
        for (VertexArrayObject vao : VAOList) {
            vao.delete();
        }

        //clean up shader here
        mainWindow.terminate();

    }
}

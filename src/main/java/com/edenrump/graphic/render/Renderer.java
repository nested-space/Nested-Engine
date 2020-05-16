package com.edenrump.graphic.render;

import com.edenrump.graphic.data.Attribute;
import com.edenrump.graphic.data.VertexArrayObject;

import java.util.List;

import static org.lwjgl.opengl.GL20.*;

/**
 * This class holds all the information required to render a mesh onto screen in OpenGL
 * <p>
 * It requires a working shader program, and should be loaded with all of the attributes you need to render the mesh.
 * <p>
 * All meshes registered with this an instance of a Renderer should have the same attributes, configuration and
 * rendering requirements
 * <p>
 * It can handle drawing with and without texture coordinates.
 */
public class Renderer {

    /**
     * The id of the shader program to use to render the entities registered with this renderer
     * <p>
     * The shader should already have been loaded, linked and compiled.
     * Objects will not render if shader program is not complete. FIXME: May throw uncaught OpenGL errors.
     */
    private int shaderProgramID;

    /**
     * List of attributes that are in use for shader program to work correctly
     */
    private List<Attribute> attributesInUse;

    /**
     * A list of vao ids that have been registered with this renderer for rending
     */
    private List<VertexArrayObject> registeredVertexArrayObjects;


    /**
     * Method to render every registered mesh to the buffer specified by the shader program
     */
    public void render() {
        //use shader program
        glUseProgram(shaderProgramID);

        //enable attributes that are needed to render the registered meshes
        for (Attribute attribute : attributesInUse) {
            glEnableVertexAttribArray(attribute.getLocation());
        }

        //render each model in turn
        for (VertexArrayObject vao : registeredVertexArrayObjects) {
            //bind VertexArrayObject
            vao.bind();
            //render


            //unbind VertexArrayObject
        }

        //disable attributes again
        for (Attribute attribute : attributesInUse) {
            glDisableVertexAttribArray(attribute.getLocation());
        }

        //unbind VertexArrayObjects to prevent subsequent accidental activity
        VertexArrayObject.unbind();

        //stop using the shader program
    }

    public void addVertexArrayObject(VertexArrayObject vao) {
        registeredVertexArrayObjects.add(vao);
    }

    public void removeVertexArrayObject(VertexArrayObject vao) {
        registeredVertexArrayObjects.remove(vao);
    }
}

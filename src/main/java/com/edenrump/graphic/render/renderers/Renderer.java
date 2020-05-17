package com.edenrump.graphic.render.renderers;

import com.edenrump.graphic.data.Attribute;
import com.edenrump.graphic.mesh.Mesh;
import com.edenrump.graphic.shaders.ShaderProgram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20C.glDisableVertexAttribArray;

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
public class Renderer implements GenericRenderer {

    /**
     * The id of the shader program to use to render the entities registered with this renderer
     * <p>
     * The shader should already have been loaded, linked and compiled.
     * Objects will not render if shader program is not complete. FIXME: May throw uncaught OpenGL errors.
     */
    private ShaderProgram shaderProgram;

    /**
     * List of attributes that are in use for shader program to work correctly
     */
    private List<Attribute> attributesInUse  = new ArrayList<>();

    /**
     * Map of VAO IDs to meshes to allow instance rendering
     */
    private Map<Integer, List<Mesh>> vaoIDMeshMap = new HashMap<>();

    /**
     * Method to construct renderer.
     * @param shaderProgram shader program that should be used to render objects with this renderer
     */
    public Renderer(ShaderProgram shaderProgram){
        this.shaderProgram = shaderProgram;
    }

    /**
     * Method to render every registered mesh to the buffer specified by the shader program
     */
    public void render() {
        //use shader program
        prepare();

        //render each model in turn
        for(Integer id : vaoIDMeshMap.keySet()){

            //get all instances of this mesh
            List<Mesh> allMeshInstances = vaoIDMeshMap.get(id);

            //enable attributes for rendering
            allMeshInstances.get(0).enableAttributes();

            for (Mesh currentMeshInstance : allMeshInstances) {
                //bind VertexArrayObject
                currentMeshInstance.getVao().bind();

                //render
                glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);


                //unbind VertexArrayObject
                currentMeshInstance.getVao().unbind();
            }

            //disable attributes for this mesh
            allMeshInstances.get(0).disableAttributes();

        }


        //disable attributes again
        for (Attribute attribute : attributesInUse) {
            glDisableVertexAttribArray(attribute.getLocation());
        }

        //unbind VAOs to prevent subsequent accidental activity

        //stop using the shader program
    }

    /**
     * Method to allow mesh to be added to vaoIDMeshMap for instance rendering
     * @param mesh mesh to be added
     */
    public void addMesh(Mesh mesh) {
        if(!vaoIDMeshMap.containsKey(mesh.getVao().getID())){
            List<Mesh> newMeshList = new ArrayList<>();
            vaoIDMeshMap.put(
                    mesh.getVao().getID(),
                    newMeshList);
        }

        vaoIDMeshMap.get(mesh.getVao().getID()).add(mesh);
    }

    /**
     * Method to allow removal of meshes from renderer
     * @param mesh mesh to be removed
     */
    public void removeMesh(Mesh mesh) {
        vaoIDMeshMap.getOrDefault(mesh.getVao().getID(), new ArrayList<>()).remove(mesh);
    }

    @Override
    public void prepare() {
        shaderProgram.use();
    }

    @Override
    public void cleanUp() {

    }
}

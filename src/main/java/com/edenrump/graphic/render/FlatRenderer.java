package com.edenrump.graphic.render;

import com.edenrump.graphic.mesh.contracts.Drawable;
import com.edenrump.graphic.shaders.ShaderProgram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;

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
public class FlatRenderer implements GenericRenderer {

    private final ShaderProgram shaderProgram;
    private final Map<Integer, List<Drawable>> vaoIDMeshMap = new HashMap<>();

    public FlatRenderer(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    @Override
    public void render() {
        prepare();
        for (Integer id : vaoIDMeshMap.keySet()) {
            List<Drawable> allMeshInstances = vaoIDMeshMap.get(id);
            allMeshInstances.get(0).bindVAO();
            for (Drawable currentMesh : allMeshInstances) {
                currentMesh.enableAttributes();
                glDrawElements(currentMesh.getDrawType(), currentMesh.getElements(), GL_UNSIGNED_INT, 0);
                currentMesh.disableAttributes();
            }
            Drawable.unbind();
        }
    }

    @Override
    public void prepare() {
        shaderProgram.use();
    }

    @Override
    public void cleanUp() {
        shaderProgram.delete();
    }

    public void addMesh(Drawable mesh) {
        if (!vaoIDMeshMap.containsKey(mesh.getVAO_ID())) {
            List<Drawable> newMeshList = new ArrayList<>();
            vaoIDMeshMap.put(
                    mesh.getVAO_ID(),
                    newMeshList);
        }
        vaoIDMeshMap.get(mesh.getVAO_ID()).add(mesh);
    }

    public void removeMesh(Drawable mesh) {
        vaoIDMeshMap.getOrDefault(mesh.getVAO_ID(), new ArrayList<>()).remove(mesh);
    }
}

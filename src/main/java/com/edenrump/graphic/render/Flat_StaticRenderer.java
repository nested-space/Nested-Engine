package com.edenrump.graphic.render;

import com.edenrump.graphic.entities.Renderable;
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
public class Flat_StaticRenderer implements GenericRenderer {

    private final ShaderProgram shaderProgram;
    private final Map<Integer, List<Renderable>> vaoIDMeshMap = new HashMap<>();

    public Flat_StaticRenderer(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    @Override
    public void render() {
        prepare();
        for (Integer id : vaoIDMeshMap.keySet()) {
            List<Renderable> allMeshInstances = vaoIDMeshMap.get(id);
            allMeshInstances.get(0).prepare();
            for (Renderable currentMesh : allMeshInstances) {
                currentMesh.update();
                glDrawElements(currentMesh.getDrawType(), currentMesh.getElements(), GL_UNSIGNED_INT, 0);
                currentMesh.finish();
            }
            Renderable.unbind();
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

    public void addMesh(Renderable mesh) {
        if (!vaoIDMeshMap.containsKey(mesh.getID())) {
            List<Renderable> newMeshList = new ArrayList<>();
            vaoIDMeshMap.put(
                    mesh.getID(),
                    newMeshList);
        }
        vaoIDMeshMap.get(mesh.getID()).add(mesh);
    }

    public void removeMesh(Renderable mesh) {
        vaoIDMeshMap.getOrDefault(mesh.getID(), new ArrayList<>()).remove(mesh);
    }
}

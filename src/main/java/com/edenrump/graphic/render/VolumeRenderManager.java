package com.edenrump.graphic.render;

import com.edenrump.graphic.data.VertexArrayObject;

/**
 * This class manages the 3-dimensional entities that should be rendered in a world.
 * <p>
 * It is currently capable of rendering the following types of mesh:
 * Static textured mesh
 * Static coloured mesh (no texture coords but every vertex has a colour associated with it)
 * <p>
 * In the future it may be able to render:
 * TODO: animated textured mesh
 * TODO: animated coloured mesh
 */
public class VolumeRenderManager {

    /**
     * The renderer necessary to render textured mesh objects.
     * <p>
     * Is only inflated when required.
     */
    Renderer texturedMeshRenderer;

    /**
     * The renderer nescessary to render coloured mesh objects.
     * <p>
     * Is only inflated when required.
     */
    Renderer colouredMeshRenderer;

    /**
     * Method to set up the textured mesh renderer with the required parameters
     */
    public void inflateTexturedMeshRenderer() {

    }

    /**
     * Method to set up the coloured mesh renderer with the required parameters
     */
    public void inflateColouredMeshRenderer(){

    }

    public void addTexturedMeshToRender(VertexArrayObject vao) {
        if (texturedMeshRenderer == null)
            inflateTexturedMeshRenderer();

        texturedMeshRenderer.addVertexArrayObject(vao);
    }

    public void addColouredMeshToRender(VertexArrayObject vao) {
        if (colouredMeshRenderer == null)
            inflateTexturedMeshRenderer();

        colouredMeshRenderer.addVertexArrayObject(vao);

    }

    public void removeTexturedMeshToRender(VertexArrayObject vao) {
        texturedMeshRenderer.removeVertexArrayObject(vao);
    }

    public void removeColouredMeshToRender(VertexArrayObject vao) {
        colouredMeshRenderer.removeVertexArrayObject(vao);
    }
}

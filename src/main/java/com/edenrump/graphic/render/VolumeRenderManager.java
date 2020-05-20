package com.edenrump.graphic.render;

import com.edenrump.graphic.mesh.Mesh;
import com.edenrump.graphic.shaders.ShaderProgram;

import java.util.HashMap;
import java.util.Map;

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

    public static final int RIGID_TEXTURED_MESH = 0x1;
    public static final int COLOUR_MESH = 0X2;

    /**
     * Map to store the supported mesh types and the renderers used to render them
     */
    private Map<Integer, Renderer> meshTypeRendererMap;

    /**
     * Constructor initialises default renderers.
     */
    public VolumeRenderManager() {
        meshTypeRendererMap = new HashMap<>();
        inflateDefaultRenderers();
    }

    /**
     * Method to set up the textured mesh renderer with the required parameters
     */
    private void inflateDefaultRenderers() {
        meshTypeRendererMap.put(RIGID_TEXTURED_MESH, new Renderer(ShaderProgram.simpleTextureShaderProgram()));
    }

    /**
     * Method to add a mesh to render, selects renderer from supported renderers
     * <p>
     * If mesh type does not have pre-installed renderer (including defaults), throws
     * IllegalStateException
     *
     * @param meshType the type of mesh as a RenderType
     * @param mesh     the mesh to be added
     */
    public void addMeshToRender(int meshType, Mesh mesh) {
        if (meshTypeRendererMap.containsKey(meshType)) {
            meshTypeRendererMap.get(meshType).addMesh(mesh);
        } else {
            throw new IllegalStateException("Unknown mesh type to add in VolumeRenderManager");
        }
    }

    /**
     * Method to remove a mesh
     *
     * @param meshType the type of mesh as a RenderType
     * @param mesh     the mesh to be removed
     */
    public void removeMeshToRender(int meshType, Mesh mesh) {
        if (meshTypeRendererMap.containsKey(meshType)) {
            meshTypeRendererMap.get(meshType).removeMesh(mesh);
        } else {
            throw new IllegalStateException("Unknown mesh type to add in VolumeRenderManager");
        }
    }
}

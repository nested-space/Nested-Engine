package com.edenrump.graphic.mesh;

import static com.edenrump.graphic.gpu.Attribute.NORMALS_ATTRIB;
import static com.edenrump.graphic.gpu.Attribute.NORMALS_ATTRIB_NAME;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;

public class CPUMesh {

    private final int floatsPerVertex;

    private float[] vertexPositions;
    private float[] vertexNormals;
    private int[] indices;

    public CPUMesh(int floatsPerVertex) {
        this.floatsPerVertex = floatsPerVertex;
    }

    public int getFloatsPerVertex() {
        return floatsPerVertex;
    }

    public float[] getVertexPositions() {
        return vertexPositions;
    }

    public void setVertexPositions(float[] vertexPositions) {
        this.vertexPositions = vertexPositions;
    }

    public float[] getVertexNormals() {
        return vertexNormals;
    }

    public void setVertexNormals(float[] vertexNormals) {
        this.vertexNormals = vertexNormals;
    }

    public int[] getIndices() {
        return indices;
    }

    public void setIndices(int[] indices) {
        this.indices = indices;
    }

    public GPUMesh commitToGPU() {
        GPUMesh gpuMesh = new GPUMesh(floatsPerVertex);
        gpuMesh.setDrawType(GL_TRIANGLES);
        gpuMesh.setPositions(vertexPositions, indices);
        gpuMesh.addAttribute(NORMALS_ATTRIB, NORMALS_ATTRIB_NAME, vertexNormals);
        return gpuMesh;
    }
}

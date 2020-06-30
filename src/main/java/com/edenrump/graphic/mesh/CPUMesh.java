/*
 * Copyright (c) 2020 Ed Eden-Rump
 *
 * This file is part of Nested Engine.
 *
 * Nested Engine is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nested Engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Nested Engine.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.edenrump.graphic.mesh;

import static com.edenrump.gpu.objects.Attribute.NORMALS_ATTRIB;
import static com.edenrump.gpu.objects.Attribute.NORMALS_ATTRIB_NAME;
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

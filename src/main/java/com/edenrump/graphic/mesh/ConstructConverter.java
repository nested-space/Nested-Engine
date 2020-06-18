package com.edenrump.graphic.mesh;

import com.edenrump.math.shape.mesh.GeometricConstruct;

public class ConstructConverter {

    public static CPUMesh convertConstructToMesh(GeometricConstruct construct) {
        float[] vertexPositions = new float[construct.getNumberOfVertices() * 3];
        float[] vertexNormals = new float[construct.getNumberOfVertices() * 3];

        for (int i = 0; i < construct.getNumberOfVertices(); i++) {
            float[] positions = construct.getVertexPosition(construct.getVertex(i).getVertexPositionIndex()).getValues();
            System.arraycopy(positions, 0, vertexPositions, i * 3, 3);

            float[] normals = construct.getVertexNormal(construct.getVertex(i).getVertexNormalIndex()).getValues();
            System.arraycopy(normals, 0, vertexNormals, i * 3, 3);
        }

        int[] indices = new int[construct.getNumberOfFaces() * 3];
        for (int i = 0; i < construct.getNumberOfFaces(); i++) {
            indices[i * 3] = construct.getVertexIndex(construct.getFace(i).getV1());
            indices[i * 3 + 1] = construct.getVertexIndex(construct.getFace(i).getV2());
            indices[i * 3 + 2] = construct.getVertexIndex(construct.getFace(i).getV3());
        }

        CPUMesh mesh = new CPUMesh(construct.getDimensions());
        mesh.setVertexPositions(vertexPositions);
        mesh.setVertexNormals(vertexNormals);
        mesh.setIndices(indices);

        return mesh;
    }

}

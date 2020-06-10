package com.edenrump.math.shape.textured;

import com.edenrump.math.arrays.ColumnVector;
import com.edenrump.math.shape.mesh.Edge;
import com.edenrump.math.shape.mesh.GeometricConstruct;

import java.util.HashMap;
import java.util.Map;

public class WrappedConstruct extends GeometricConstruct {

    private final Map<Integer, ColumnVector> vertexTextureCoordinates = new HashMap<>();

    public WrappedConstruct(int coordinateType) {
        super(coordinateType);
    }

    public void addVertexTextureCoordinate(int vertex, float x, float y) {
        ColumnVector vt = new ColumnVector(x, y);

        if (this.vertices.size() <= vertex || vertex < 0)
            throw new IllegalArgumentException("Cannot create face with vertex out of range." +
                    "\nVertex added: number " + vertex + " | Current number of vertices: " + vertices.size());

        this.vertexTextureCoordinates.put(vertex, vt);
    }

    @Override
    protected int addMidPoint(Edge edge) {
        int vertex = super.addMidPoint(edge);

        ColumnVector vt1 = vertexTextureCoordinates.get(edge.getV1().getVertexPositionIndex());
        ColumnVector vt2 = vertexTextureCoordinates.get(edge.getV2().getVertexPositionIndex());
        ColumnVector midPointPosition = vt1.add(vt2).scale(0.5f);
        addVertexTextureCoordinate(
                vertex,
                midPointPosition.getValue(0),
                midPointPosition.getValue(1));

        return vertex;
    }

    public int getNumberOfTextureCoordinates() {
        return vertexTextureCoordinates.keySet().size();
    }

    public ColumnVector getTextureCoordinate(int vertexIndex) {
        return vertexTextureCoordinates.get(vertexIndex);
    }


}

package com.edenrump.math.shape.mesh;

public class Vertex {

    private final int vertexPositionIndex;
    private final int vertexNormalIndex;

    public Vertex(int vertexPositionIndex, int vertexNormalIndex) {
        if (vertexPositionIndex < 0 || vertexNormalIndex < 0)
            throw new IllegalArgumentException("Cannot create vertex with negative position or normal indices");

        this.vertexPositionIndex = vertexPositionIndex;
        this.vertexNormalIndex = vertexNormalIndex;
    }

    public int getVertexPositionIndex() {
        return vertexPositionIndex;
    }

    public int getVertexNormalIndex() {
        return vertexNormalIndex;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vertex) {
            Vertex other = (Vertex) obj;
            return this.getVertexPositionIndex() == other.getVertexPositionIndex() &&
                    this.getVertexNormalIndex() == other.getVertexNormalIndex();
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return vertexPositionIndex * 17 +
                vertexNormalIndex * 19;
    }

    @Override
    public String toString() {
        return "Vertex(vp=" + vertexPositionIndex + ", vn=" + vertexNormalIndex + ")";
    }
}

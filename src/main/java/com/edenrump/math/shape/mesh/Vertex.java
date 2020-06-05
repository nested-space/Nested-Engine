package com.edenrump.math.shape.mesh;

public class Vertex {

    int vertexPositionIndex;
    int vertexNormalIndex;

    public Vertex(int vertexPositionIndex, int vertexNormalIndex) {
        if (vertexPositionIndex < 0 || vertexNormalIndex < 0)
            throw new IllegalArgumentException("Cannot create vertex with negative position or normal indices");

        this.vertexPositionIndex = vertexPositionIndex;
        this.vertexNormalIndex = vertexNormalIndex;
    }

    public int getVertexPositionIndex() {
        return vertexPositionIndex;
    }

    public void setVertexPositionIndex(int vertexPositionIndex) {
        this.vertexPositionIndex = vertexPositionIndex;
    }

    public int getVertexNormalIndex() {
        return vertexNormalIndex;
    }

    public void setVertexNormalIndex(int vertexNormalIndex) {
        this.vertexNormalIndex = vertexNormalIndex;
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

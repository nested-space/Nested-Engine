package com.edenrump.math.shape.textured;

import com.edenrump.math.shape.mesh.Vertex;

public class WrappedVertex extends Vertex {

    int vertexTextureCoordinate;

    public WrappedVertex(int vertexPositionIndex, int vertexNormalIndex, int vertexTextureCoordinate) {
        super(vertexPositionIndex, vertexNormalIndex);

        if (vertexPositionIndex < 0 || vertexNormalIndex < 0 || vertexTextureCoordinate < 0)
            throw new IllegalArgumentException("Cannot create vertex with negative position or normal indices");

        this.vertexTextureCoordinate = vertexTextureCoordinate;
    }

    public int getVertexTextureCoordinate() {
        return vertexTextureCoordinate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WrappedVertex) {
            WrappedVertex other = (WrappedVertex) obj;
            return this.getVertexPositionIndex() == other.getVertexPositionIndex() &&
                    this.getVertexNormalIndex() == other.getVertexNormalIndex() &&
                    this.getVertexTextureCoordinate() == other.getVertexTextureCoordinate();
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return getVertexPositionIndex() * 17 +
                getVertexNormalIndex() * 19 +
                getVertexTextureCoordinate() * 23;
    }

    @Override
    public String toString() {
        return "Vertex(vp=" + getVertexPositionIndex() + ", vn=" + getVertexNormalIndex() +
                ", vt= " + getVertexTextureCoordinate() + ")";
    }

}

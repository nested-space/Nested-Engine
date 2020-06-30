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

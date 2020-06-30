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

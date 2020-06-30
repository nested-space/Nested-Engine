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

public class Edge {

    Vertex v1;
    Vertex v2;

    public Edge(Vertex vertex1, Vertex vertex2) {
        if (vertex1 == null || vertex2 == null)
            throw new IllegalArgumentException("Cannot create edge with null vertices");

        this.v1 = vertex1;
        this.v2 = vertex2;
    }

    public Vertex getV1() {
        return v1;
    }

    public Vertex getV2() {
        return v2;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Edge) {
            Edge other = (Edge) obj;
            return this.getV1().equals(other.getV1()) && this.getV2().equals(other.getV2()) ||
                    this.getV2().equals(other.getV1()) && this.getV1().equals(other.getV2() );
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return v1.hashCode() * 29 + v2.hashCode() * 29;
    }

    @Override
    public String toString() {
        return "Edge: " + v1 + " to " + v2;
    }
}

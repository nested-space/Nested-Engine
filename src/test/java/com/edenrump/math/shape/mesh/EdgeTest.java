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

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EdgeTest {

    final Random r = new Random(10258);
    final Vertex v1 = new Vertex(r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE));
    final Vertex v2 = new Vertex(r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE));
    final Edge edge1 = new Edge(v1, v2);
    final Edge edge2 = new Edge(v1, v2);

    @Test
    public void constructorTest() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new Edge(null, new Vertex(1, 1)));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Edge(new Vertex(1, 1), null));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Edge(null, null));
    }

    @Test
    public void equalsTest() {
        Assert.assertEquals(edge1, edge2);
        Assert.assertEquals(edge1, new Edge(v2, v1)); //edges are meant to be direction-independent
    }

    @Test
    public void hashcodeTest() {
        Assert.assertEquals(edge1.hashCode(), edge2.hashCode());
    }

    @Test
    public void mapTest() {
        Map<Edge, Integer> edges = new HashMap<>();
        edges.put(edge1, 1);
        edges.put(edge2, 2);
        Assert.assertTrue(edges.containsKey(edge1));
        Assert.assertTrue(edges.containsKey(edge2));

        Edge backwardsEdge = new Edge(edge1.v2, edge1.v1);
        Assert.assertTrue(edges.containsKey(backwardsEdge));
    }

}

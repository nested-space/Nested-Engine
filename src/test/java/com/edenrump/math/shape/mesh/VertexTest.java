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

import java.util.Random;

public class VertexTest {

    final Random r = new Random(10258);
    final Vertex v1 = new Vertex(r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE));
    final Vertex v2 = new Vertex(r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE));
    final Vertex v3 = new Vertex(r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE));
    final Vertex v4 = new Vertex(r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE));

    @Test
    public void constructorTest() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new Vertex(-1, r.nextInt(Integer.MAX_VALUE)));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Vertex(r.nextInt(Integer.MAX_VALUE), -1));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Vertex(-1, -1));
    }

    @Test
    public void equalsTest() {
        Vertex v5 = new Vertex(v1.getVertexPositionIndex(), v1.getVertexNormalIndex());
        Assert.assertEquals(v1, v1);
        Assert.assertEquals(v1, v5);
        Assert.assertEquals(v3, v3);
        Assert.assertNotEquals(v1, v3);
        Assert.assertNotEquals(v2, v4);
    }

    @Test
    public void hashcodeTest() {
        Assert.assertEquals(v1.hashCode(), v1.hashCode());
        Assert.assertEquals(v2.hashCode(), v2.hashCode());
        Assert.assertNotEquals(v1.hashCode(), v2.hashCode());
        Assert.assertNotEquals(v3.hashCode(), v4.hashCode());
    }

}

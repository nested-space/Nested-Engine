package com.edenrump.math.shape.mesh;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Random;

/*
 * Copyright (c) 2020 Ed Eden-Rump
 *     This file is part of Nested Engine.
 *
 *     Nested Engine is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Nested Engine is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Nested Engine.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     @Author Ed Eden-Rump
 *
 */

public class FaceTest {

    final Random r = new Random(10258);
    final Vertex v1 = new Vertex(r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE));
    final Vertex v2 = new Vertex(r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE));
    final Vertex v3 = new Vertex(r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE));

    @Test
    public void constructTest() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new Face(v1, v1, v2));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Face(null, v1, v2));
    }

    @Test
    public void equalsTest() {
        Assert.assertEquals(new Face(v1, v2, v3), new Face(v1, v2, v3));
        Assert.assertNotEquals(new Face(v1, v2, v3), new Face(v2, v1, v3));
    }

    @Test
    public void hashcodeTest() {
        Assert.assertEquals(new Face(v1, v2, v3).hashCode(), new Face(v1, v2, v3).hashCode());
    }
}

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

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Random;

public class WrappedVertexTest {

    final Random r = new Random(10258);
    final WrappedVertex wv1 =
            new WrappedVertex(r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE));
    final WrappedVertex wv1a =
            new WrappedVertex(wv1.getVertexPositionIndex(), wv1.getVertexNormalIndex(), wv1.getVertexTextureCoordinate());
    final WrappedVertex wv2 =
            new WrappedVertex(r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE));
    final WrappedVertex wv3 =
            new WrappedVertex(r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE));

    @Test
    public void constructTest() {
        Assert.assertThrows(IllegalArgumentException.class, () ->
                new WrappedVertex(1, 1, -55));
    }

    @Test
    public void equalsTest() {
        Assert.assertEquals(wv1, wv1a);
        Assert.assertNotEquals(wv1, wv3);
    }

    @Test
    public void haschodeTest() {
        Assert.assertEquals(wv1.hashCode(), wv1.hashCode());
        Assert.assertEquals(wv2.hashCode(), wv2.hashCode());
        Assert.assertNotEquals(wv1.hashCode(), wv2.hashCode());
        Assert.assertNotEquals(wv2.hashCode(), wv3.hashCode());
    }
}

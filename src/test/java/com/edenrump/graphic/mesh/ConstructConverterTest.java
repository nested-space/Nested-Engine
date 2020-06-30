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

package com.edenrump.graphic.mesh;

import com.edenrump.math.shape.mesh.GeometricConstructTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ConstructConverterTest {

    @Test
    public void convertConstructToMeshTest() {
        CPUMesh mesh = ConstructConverter.convertConstructToMesh(
                GeometricConstructTest.getTestSquare()
        );

        Assert.assertEquals(mesh.getFloatsPerVertex(), 3);
        Assert.assertEquals(mesh.getIndices().length, 6);
        Assert.assertEquals(mesh.getVertexPositions().length, 4 * 3);
        Assert.assertEquals(mesh.getVertexNormals().length, 4 * 3);
    }

}

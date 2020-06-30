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

package com.edenrump.loaders;

import com.edenrump.math.shape.textured.WrappedConstruct;
import org.testng.Assert;
import org.testng.annotations.Test;

public class OBJFileTest {

    @Test(priority = 1)
    public void loadFileTest() {
        Assert.assertThrows(RuntimeException.class, () -> new OBJFile("no file to load"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new OBJFile(null));
    }

    @Test(priority = 2)
    public void getMeshTest() {
        OBJFile squareFile = new OBJFile("src/test/resources/models/Square.obj");
        WrappedConstruct square = squareFile.getMesh();

        Assert.assertEquals(square.getNumberOfFaces(), 2);
        Assert.assertEquals(square.getNumberOfVertices(), 4);

        OBJFile cubeFile = new OBJFile("src/test/resources/models/Cube.obj");
        Assert.assertEquals(cubeFile.getMesh().getNumberOfFaces(), 12);
        Assert.assertEquals(cubeFile.getMesh().getNumberOfVertices(), 24);
    }

    @Test(priority = 3)
    public void meshFormatTests() {
        Assert.assertThrows(RuntimeException.class, () -> //if lines contain text that's not a float, reject
                new OBJFile("src/test/resources/models/Cube_BadlyFormatted_VertexPositions_NotFloats.obj"));

        Assert.assertThrows(RuntimeException.class, () -> //if vertex positions contain wrong number of floats, reject
                new OBJFile("src/test/resources/models/Cube_BadlyFormatted_VertexPositions_IncorrectDimensions.obj"));

        Assert.assertThrows(RuntimeException.class, () -> //if faces refer to non-existent vertex positions, reject
                new OBJFile("src/test/resources/models/Cube_BadlyConstructed_NonExistentPositions.obj"));

        Assert.assertThrows(RuntimeException.class, () -> //polygon faces don't contain enough vertices, reject
                new OBJFile("src/test/resources/models/Cube_Face_TooFewVerticesPerFace.obj"));
    }
}

package com.edenrump.loaders;

import com.edenrump.loaders.OBJFile;
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

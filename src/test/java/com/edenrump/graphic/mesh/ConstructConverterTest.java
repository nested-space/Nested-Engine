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

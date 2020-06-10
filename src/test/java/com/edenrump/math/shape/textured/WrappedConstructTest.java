package com.edenrump.math.shape.textured;

import com.edenrump.math.shape.mesh.Vertex;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.edenrump.math.shape.mesh.GeometricConstruct.CARTESIAN;
import static com.edenrump.math.shape.mesh.GeometricConstruct.POLAR;

public class WrappedConstructTest {

    @Test
    public void constructTest() {
        WrappedConstruct construct = new WrappedConstruct(POLAR);
        Assert.assertEquals(construct.getCoordinateType(), POLAR);

        construct = new WrappedConstruct(CARTESIAN);
        Assert.assertEquals(construct.getCoordinateType(), CARTESIAN);

        Assert.assertThrows(IllegalArgumentException.class, () -> new WrappedConstruct(3));
        Assert.assertThrows(IllegalArgumentException.class, () -> new WrappedConstruct(-1));
    }

    @Test
    public void addTextureCoordinateTest() {
        WrappedConstruct construct = getTestSquare();
        Assert.assertThrows(IllegalArgumentException.class, () -> //vertex index is too high
                construct.addVertexTextureCoordinate(17, 0.5f, 0.7f));
        Assert.assertThrows(IllegalArgumentException.class, () -> //vertex index is too high
                construct.addVertexTextureCoordinate(-1, 0.5f, 0.7f));

        Assert.assertEquals(construct.getNumberOfTextureCoordinates(), 4);

        construct.addVertexTextureCoordinate(0, 0, 1); //should overwrite previous coordinate
        Assert.assertEquals(construct.getNumberOfTextureCoordinates(), 4);
    }

    private WrappedConstruct getTestSquare(){
        float[][] points = squarePoints();

        WrappedConstruct square = new WrappedConstruct(POLAR);
        square.addVertexPosition(points[0][0], points[0][1], points[0][2]);
        square.addVertexPosition(points[1][0], points[1][1], points[1][2]);
        square.addVertexPosition(points[2][0], points[2][1], points[2][2]);
        square.addVertexPosition(points[3][0], points[3][1], points[3][2]);

        square.addVertexNormal(points[0][0], points[0][1], points[0][2]);
        square.addVertexNormal(points[1][0], points[1][1], points[1][2]);
        square.addVertexNormal(points[2][0], points[2][1], points[2][2]);
        square.addVertexNormal(points[3][0], points[3][1], points[3][2]);

        int v0 = square.addVertex(new Vertex(0, 0));
        int v1 = square.addVertex(new Vertex(1, 1));
        int v2 = square.addVertex(new Vertex(2, 2));
        int v3 = square.addVertex(new Vertex(3, 3));

        square.addFace(v0, v1, v2);
        square.addFace(v2, v1, v3);

        square.addVertexTextureCoordinate(0, 0, 0);
        square.addVertexTextureCoordinate(1, 1, 0);
        square.addVertexTextureCoordinate(2, 0, 1);
        square.addVertexTextureCoordinate(3, 1, 1);
        return square;
    }

    private float[][] squarePoints() {
        //  0-----2
        //  |  o  |    o origin, n = point; all points in the XY plane so inclination angle is 90° (π/2)
        //  1-----3
        return new float[][]{
                new float[]{1, (float) Math.PI / 2, (float) (Math.PI / 4)},
                new float[]{1, (float) Math.PI / 2, (float) (3f * (Math.PI / 4))},
                new float[]{1, (float) Math.PI / 2, (float) (-Math.PI / 4)},
                new float[]{1, (float) Math.PI / 2, (float) (3f * -Math.PI / 4)}
        };
    }
}

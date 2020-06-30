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

import static com.edenrump.math.shape.mesh.GeometricConstruct.CARTESIAN;
import static com.edenrump.math.shape.mesh.GeometricConstruct.POLAR;

public class GeometricConstructTest {

    @Test
    public void constructTest() {
        GeometricConstruct construct = new GeometricConstruct(POLAR);
        Assert.assertEquals(construct.getCoordinateType(), POLAR);

        construct = new GeometricConstruct(CARTESIAN);
        Assert.assertEquals(construct.getCoordinateType(), CARTESIAN);

        Assert.assertThrows(IllegalArgumentException.class, () -> new GeometricConstruct(3));
        Assert.assertThrows(IllegalArgumentException.class, () -> new GeometricConstruct(-1));
    }

    @Test
    public void coordinateTypeTest() {
        GeometricConstruct construct = new GeometricConstruct(POLAR);
        Assert.assertEquals(construct.getCoordinateType(), POLAR);

        construct.setCoordinateType(POLAR);
        Assert.assertEquals(POLAR, construct.getCoordinateType());

        construct.setCoordinateType(CARTESIAN);
        Assert.assertEquals(construct.getCoordinateType(), CARTESIAN);

        Assert.assertThrows(IllegalArgumentException.class, () -> new GeometricConstruct(POLAR).setCoordinateType(-1));
    }

    @Test
    public void vertexPositionsTest() {
        GeometricConstruct construct = getTestSquare();
        Assert.assertEquals(construct.vertexNormals.size(), 4);
        Assert.assertEquals(construct.vertexPositions.size(), 4);
        Assert.assertEquals(construct.vertices.size(), 4);
        Assert.assertEquals(construct.coordinateType, POLAR);
        Assert.assertEquals(construct.faces.size(), 2);

        Assert.assertTrue(construct.vertices.contains(new Vertex(0, 0)));
        Assert.assertTrue(construct.vertices.contains(new Vertex(1, 1)));
        Assert.assertTrue(construct.vertices.contains(new Vertex(2, 2)));
        Assert.assertTrue(construct.vertices.contains(new Vertex(3, 3)));
    }

    @Test
    public void verticesTest() {
        GeometricConstruct construct = getTestSquare();
        Assert.assertTrue(construct.getVertexIndex(new Vertex(0, 0)) <= 3);
        Assert.assertTrue(construct.getVertexIndex(new Vertex(1, 1)) <= 3);
        Assert.assertTrue(construct.getVertexIndex(new Vertex(2, 2)) <= 3);
        Assert.assertTrue(construct.getVertexIndex(new Vertex(3, 3)) <= 3);
        Assert.assertTrue(construct.getVertexIndex(new Vertex(0, 0)) >= 0);
        Assert.assertTrue(construct.getVertexIndex(new Vertex(1, 1)) >= 0);
        Assert.assertTrue(construct.getVertexIndex(new Vertex(2, 2)) >= 0);
        Assert.assertTrue(construct.getVertexIndex(new Vertex(3, 3)) >= 0);

        Assert.assertThrows(IllegalArgumentException.class,
                () -> construct.addVertex(new Vertex(5, 7)));

        int newVertex = construct.addVertex(new Vertex(0, 1));
        Assert.assertEquals(newVertex, 4);
        Assert.assertEquals(construct.addVertex(new Vertex(0, 1)), 4);
        //still 4 because same as before
    }

    @Test
    public void addFaceTest() {
        GeometricConstruct construct = getTestSquare();
        Assert.assertEquals(construct.getNumberOfFaces(), 2);
        construct.addFace(1, 2, 3);
        Assert.assertEquals(construct.getNumberOfFaces(), 3);
        construct.addFace(1, 2, 3);
        Assert.assertEquals(construct.getNumberOfFaces(), 3); //same as already added 1, 1, 3 face
    }

    @Test
    protected void addMidPointTest() {
        GeometricConstruct construct = getTestSquare();
        Edge edge = new Edge(construct.vertices.get(0), construct.vertices.get(2)); //opposite corners
        int vp1 = construct.vertices.get(construct.addMidPoint(edge)).getVertexNormalIndex();
        Assert.assertEquals(construct.vertexPositions.get(vp1).getValue(0), 1.0f); //no change
        Assert.assertEquals(construct.vertexPositions.get(vp1).getValue(2), 0.0f); //zero (centre)
    }

    @Test
    public void convertToCartesianCoordinatesTest() {
        GeometricConstruct construct = getTestSquare();
        construct.setCoordinateType(CARTESIAN);
        Assert.assertEquals(construct.getCoordinateType(), CARTESIAN);
        Assert.assertEquals(construct.getNumberOfFaces(), 2);
        Assert.assertEquals(construct.getNumberOfVertices(), 4);

        //test to see whether conversion to coordinates has kept the opposite corners equidistant around the centre
        float THRESHOLD = 0.00001f;
        Edge edge = new Edge(construct.vertices.get(0), construct.vertices.get(2));
        int vp1 = construct.vertices.get(construct.addMidPoint(edge)).getVertexPositionIndex();

        Assert.assertTrue( //root2/2 within float rounding errors
                construct.vertexPositions.get(vp1).getValue(0) - Math.sqrt(2) / 2f < THRESHOLD
        );

        Assert.assertTrue( //zero z within float rounding errors
                construct.vertexPositions.get(vp1).getValue(2) < THRESHOLD
        );
    }

    @Test
    public void convertToPolarCoordinatesTest() {
        GeometricConstruct construct = getTestSquare();
        construct.setCoordinateType(CARTESIAN);
        construct.setCoordinateType(POLAR);
        Assert.assertEquals(construct.getCoordinateType(), POLAR);
        Assert.assertEquals(construct.getNumberOfFaces(), 2);
        Assert.assertEquals(construct.getNumberOfVertices(), 4);

        //test to see whether conversion to coordinates has kept r and inclination angle consistent
        float THRESHOLD = 0.0001f;
        Edge edge = new Edge(construct.vertices.get(0), construct.vertices.get(2));
        int vp1 = construct.vertices.get(construct.addMidPoint(edge)).getVertexPositionIndex();
        Assert.assertTrue(//r is 1 within float rounding errors
                construct.vertexPositions.get(vp1).getValue(0) - 1 < THRESHOLD
        );

        Assert.assertTrue( //inclination angle is pi/2 within float rounding errors
                construct.vertexPositions.get(vp1).getValue(1) - (Math.PI / 2) < THRESHOLD
        );

        //test whether large number of conversions results in loss of accuracy
        for (int i = 0; i < 500; i++) {
            if (i % 2 == 0) {
                construct.setCoordinateType(CARTESIAN);
            } else {
                construct.setCoordinateType(POLAR);
            }
        }

        Assert.assertTrue(//r is 1 within float rounding errors
                construct.vertexPositions.get(vp1).getValue(0) - 1 < THRESHOLD
        );
        Assert.assertTrue( //inclination angle is pi/2 within float rounding errors
                construct.vertexPositions.get(vp1).getValue(1) - (Math.PI / 2) < THRESHOLD
        );
    }

    @Test
    public void subdivideMeshTest() {
        GeometricConstruct square = getTestSquare();
        square.setCoordinateType(CARTESIAN);
        Assert.assertEquals(square.getNumberOfFaces(), 2);
        square.subdivideMesh();
        Assert.assertEquals(square.getNumberOfFaces(), 8);
        square.subdivideMesh();
        Assert.assertEquals(square.getNumberOfFaces(), 32);

        square = getTestSquare();
        Assert.assertEquals(square.getNumberOfFaces(), 2);
        square.subdivideMesh();
        Assert.assertEquals(square.getNumberOfFaces(), 8);
        square.subdivideMesh();
        Assert.assertEquals(square.getNumberOfFaces(), 32);
    }

    public static GeometricConstruct getTestSquare() {
        float[][] points = squarePoints();

        GeometricConstruct square = new GeometricConstruct(POLAR);
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

        return square;
    }

    private static float[][] squarePoints() {
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

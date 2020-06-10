package com.edenrump.math.shape.solids;

import com.edenrump.math.arrays.ColumnVector;
import com.edenrump.math.shape.mesh.Face;
import com.edenrump.math.shape.mesh.GeometricConstruct;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.edenrump.math.shape.mesh.GeometricConstruct.CARTESIAN;

public class IcosahedronTest {

    @Test
    public void constructTest() {
        Icosahedron ico = new Icosahedron();
        Assert.assertEquals(ico.getRadius(), 1);

        ico = new Icosahedron(25f);
        Assert.assertEquals(ico.getRadius(), 25f);

        Assert.assertThrows(IllegalArgumentException.class, () -> new Icosahedron(-25f));
    }

    @Test
    public void meshTest() {
        Icosahedron ico = new Icosahedron();
        GeometricConstruct mesh = ico.getMesh();
        mesh.setCoordinateType(CARTESIAN);

        Assert.assertEquals(mesh.getNumberOfVertices(), 12);
        Assert.assertEquals(mesh.getNumberOfFaces(), 20);

        float THRESHOLD = 0.000001f;
        Face firstFace = mesh.getFaces().get(0);
        float faceArea = calculateArea(firstFace, mesh);
        float faceEdgeLength = calculateLength(firstFace.getV1().getVertexPositionIndex(),
                firstFace.getV2().getVertexPositionIndex(), mesh);
        for (Face face : mesh.getFaces()) {
            int v1 = face.getV1().getVertexPositionIndex();
            int v2 = face.getV2().getVertexPositionIndex();
            int v3 = face.getV3().getVertexPositionIndex();
            Assert.assertTrue(faceArea - calculateArea(face, mesh) < THRESHOLD);            //faces should all be the same size
            Assert.assertTrue(faceEdgeLength - calculateLength(v1, v2, mesh) < THRESHOLD);  //edge lengths are all the same
            Assert.assertTrue(faceEdgeLength - calculateLength(v2, v3, mesh) < THRESHOLD);
            Assert.assertTrue(faceEdgeLength - calculateLength(v3, v1, mesh) < THRESHOLD);
        }
    }

    private float calculateArea(Face face, GeometricConstruct mesh) {
        ColumnVector P = mesh.getVertexPosition(face.getV1().getVertexPositionIndex());
        ColumnVector Q = mesh.getVertexPosition(face.getV2().getVertexPositionIndex());
        ColumnVector R = mesh.getVertexPosition(face.getV3().getVertexPositionIndex());

        ColumnVector PQ = Q.subtract(P);
        ColumnVector PR = R.subtract(P);

        return 0.5f * PR.cross(PQ).length();
    }

    private float calculateLength(int v1, int v2, GeometricConstruct mesh) {
        ColumnVector A = mesh.getVertexPosition(v1);
        ColumnVector B = mesh.getVertexPosition(v2);

        return A.getDistanceToOther(B);
    }
}

package com.edenrump.math.shape.solids;

import com.edenrump.math.arrays.ColumnVector;
import com.edenrump.math.shape.mesh.GeometricConstruct;
import com.edenrump.math.shape.mesh.ShadingType;
import com.edenrump.math.shape.mesh.Vertex;

import java.util.ArrayList;
import java.util.List;

import static com.edenrump.math.shape.mesh.GeometricConstruct.CARTESIAN;

public class Icosahedron {

    private ShadingType shadingType;
    private float radius;
    private GeometricConstruct construct;

    public Icosahedron() {
        this(1f);
    }

    public Icosahedron(float radius) {
        this(radius, ShadingType.SMOOTH);
    }

    public Icosahedron(float radius, ShadingType type) {
        if (radius <= 0)
            throw new IllegalArgumentException("Cannot create a shape with negative or zero size");

        this.radius = radius;
        this.shadingType = type;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        if (this.radius == radius) return;

        construct = null;
        this.radius = radius;
    }

    public ShadingType getShadingType() {
        return shadingType;
    }

    public void setShadingType(ShadingType shadingType) {
        if (this.shadingType == shadingType) return;

        this.construct = null;
        this.shadingType = shadingType;
    }

    public void cacheMesh() {
        construct = getMesh();
    }

    public GeometricConstruct getMesh() {
        if (construct != null) return construct;

        if (shadingType == ShadingType.FLAT) {
            construct = createFlatShadedMesh();
        } else {
            construct = createSmoothShadedMesh();
        }

        return construct;
    }

    private GeometricConstruct createSmoothShadedMesh() {
        GeometricConstruct geometricConstruct = new GeometricConstruct(CARTESIAN);
        geometricConstruct.setVertexPositions(getVertexCoordinatesCartesian());
        geometricConstruct.setVertexNormals(getVertexCoordinatesCartesian());

        for (int i = 0; i < 12; i++) {
            geometricConstruct.addVertex(new Vertex(i, i));
        }

        for (Triangle face : getFaces()) {
            geometricConstruct.addFace(face.v1, face.v2, face.v3);
        }

        return geometricConstruct;
    }

    private GeometricConstruct createFlatShadedMesh() {
        GeometricConstruct geometricConstruct = new GeometricConstruct(CARTESIAN);
        geometricConstruct.setVertexPositions(getVertexCoordinatesCartesian());
        geometricConstruct.setVertexNormals(getFaceNormals());
        List<Triangle> faces = getFaces();
        for (Triangle face : faces) {
            int faceIndex = faces.indexOf(face);
            int v0 = geometricConstruct.addVertex(new Vertex(face.v1, faceIndex));
            int v1 = geometricConstruct.addVertex(new Vertex(face.v2, faceIndex));
            int v2 = geometricConstruct.addVertex(new Vertex(face.v3, faceIndex));
            geometricConstruct.addFace(v0, v1, v2);
        }
        return geometricConstruct;
    }

    private List<ColumnVector> getVertexCoordinatesCartesian() {
        List<ColumnVector> vertexPositions = new ArrayList<>();

        float t = (float) ((1.0 + Math.sqrt(5.0)) / 2.0);

        vertexPositions.add(new ColumnVector(-1, t, 0));
        vertexPositions.add(new ColumnVector(1, t, 0));
        vertexPositions.add(new ColumnVector(-1, -t, 0));
        vertexPositions.add(new ColumnVector(1, -t, 0));

        vertexPositions.add(new ColumnVector(0, -1, t));
        vertexPositions.add(new ColumnVector(0, 1, t));
        vertexPositions.add(new ColumnVector(0, -1, -t));
        vertexPositions.add(new ColumnVector(0, 1, -t));

        vertexPositions.add(new ColumnVector(t, 0, -1));
        vertexPositions.add(new ColumnVector(t, 0, 1));
        vertexPositions.add(new ColumnVector(-t, 0, -1));
        vertexPositions.add(new ColumnVector(-t, 0, 1));

        return vertexPositions;
    }

    private List<ColumnVector> getFaceNormals() {
        ColumnVector[] vn = getVertexCoordinatesCartesian().toArray(new ColumnVector[0]);
        List<ColumnVector> faceNormals = new ArrayList<>();

        for (Triangle face : getFaces()) {
            faceNormals.add(averageNormal(vn[face.v1], vn[face.v2], vn[face.v3]));
        }

        return faceNormals;
    }

    private List<Triangle> getFaces() {
        List<Triangle> faces = new ArrayList<>();

        // 5 faces around point 0
        faces.add(new Triangle(0, 11, 5));
        faces.add(new Triangle(0, 5, 1));
        faces.add(new Triangle(0, 1, 7));
        faces.add(new Triangle(0, 7, 10));
        faces.add(new Triangle(0, 10, 11));

        // 5 adjacent faces
        faces.add(new Triangle(1, 5, 9));
        faces.add(new Triangle(5, 11, 4));
        faces.add(new Triangle(11, 10, 2));
        faces.add(new Triangle(10, 7, 6));
        faces.add(new Triangle(7, 1, 8));

        // 5 faces around point 3
        faces.add(new Triangle(3, 9, 4));
        faces.add(new Triangle(3, 4, 2));
        faces.add(new Triangle(3, 2, 6));
        faces.add(new Triangle(3, 6, 8));
        faces.add(new Triangle(3, 8, 9));

        // 5 adjacent faces
        faces.add(new Triangle(4, 9, 5));
        faces.add(new Triangle(2, 4, 11));
        faces.add(new Triangle(6, 2, 10));
        faces.add(new Triangle(8, 6, 7));
        faces.add(new Triangle(9, 8, 1));

        return faces;
    }

    private ColumnVector averageNormal(ColumnVector v1, ColumnVector v2, ColumnVector v3) {
        return v1.add(v2).add(v3).scale(1f / 3f);
    }

    private static class Triangle {
        int v1, v2, v3;

        Triangle(int v1, int v2, int v3) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
        }
    }

}

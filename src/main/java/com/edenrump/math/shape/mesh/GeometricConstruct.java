package com.edenrump.math.shape.mesh;

import com.edenrump.math.arrays.ColumnVector;
import com.edenrump.math.calculations.Volume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeometricConstruct {

    public static final int CARTESIAN = 0x0;
    public static final int POLAR = 0x1;
    protected int coordinateType = -1;
    protected int dimensions;

    protected List<ColumnVector> vertexPositions = new ArrayList<>();
    protected List<ColumnVector> vertexNormals = new ArrayList<>();
    protected List<Face> faces = new ArrayList<>();
    protected List<Vertex> vertices = new ArrayList<>();

    public GeometricConstruct(int coordinateType) {
        setCoordinateType(coordinateType);
        dimensions = 3;
    }

    public int getCoordinateType() {
        return coordinateType;
    }

    public void setCoordinateType(int newCoordinateType) {
        if (newCoordinateType != CARTESIAN && newCoordinateType != POLAR) //supported types
            throw new IllegalArgumentException("Use GeometricConstruct.CARTESIAN and GeometricConstruct.POLAR" +
                    " to defined coordinate type. Unsupported type specified.");

        if (this.coordinateType == newCoordinateType) return;

        if (newCoordinateType == POLAR) {
            convertToPolarCoordinates();
        } else {
            convertToCartesianCoordinates();
        }
    }

    public void setVertexPositions(List<ColumnVector> positions) {
        this.vertexPositions.clear();
        for (ColumnVector vp : positions) {
            if (vp.getDimensions() != 3)
                throw new IllegalArgumentException("MeshGeometry does not support non-3D coordinates");

            addVertexPosition(vp.getValue(0),
                    vp.getValue(1),
                    vp.getValue(2));
        }
    }

    public void setVertexNormals(List<ColumnVector> normals) {
        this.vertexNormals.clear();
        for (ColumnVector vn : normals) {
            if (vn.getDimensions() != 3)
                throw new IllegalArgumentException("MeshGeometry does not support non-3D normals");

            addVertexNormal(vn.getValue(0),
                    vn.getValue(1),
                    vn.getValue(2));
        }
    }

    /**
     * Creates a new vertex position which is stored as part of the construct.
     * <p>
     * Does not create a vertex proper, which should be defined as a combination of position and normal using
     * addVertex(Vertex vertex)
     * <p>
     * Polar coordinates should define angles in radians.
     *
     * @param x the x coordinate, or the radial distance
     * @param y the y cooridnate, or the inclination (angle from z) in radians
     * @param z the z coordinate, or the azimuthal angle (angle from x-y plane) in radians
     * @return the index of the vertex position created.
     */
    public int addVertexPosition(float x, float y, float z) {
        ColumnVector vp = new ColumnVector(x, y, z);
        this.vertexPositions.add(vp);
        return this.vertexPositions.indexOf(vp);
    }

    public ColumnVector getVertexPosition(int vertexPositionIndex) {
        return vertexPositions.get(vertexPositionIndex);
    }

    public int addVertexNormal(float x, float y, float z) {
        ColumnVector vn = new ColumnVector(x, y, z);
        this.vertexNormals.add(vn);
        return this.vertexNormals.indexOf(vn);
    }

    public ColumnVector getVertexNormal(int vertexNormalIndex) {
        return vertexNormals.get(vertexNormalIndex);
    }

    public int addVertex(Vertex vertex) {
        if (!this.vertices.contains(vertex)) {
            if (vertexNormals.size() <= vertex.getVertexNormalIndex())
                throw new IllegalArgumentException("Vertex references vertex normal that is out of range. " +
                        "\nExpected index of " + vertex.getVertexNormalIndex() + " in range 0 to " + vertexNormals.size());

            if (vertexPositions.size() <= vertex.getVertexPositionIndex())
                throw new IllegalArgumentException("Vertex references vertex position that is out of range. " +
                        "\nExpected index of " + vertex.getVertexPositionIndex() + " in range 0 to " + vertexPositions.size());

            this.vertices.add(vertex);
        }

        return this.vertices.indexOf(vertex);
    }

    public int getVertexIndex(Vertex vertex) {
        return vertices.indexOf(vertex);
    }

    public int getNumberOfVertices() {
        return vertices.size();
    }

    public int addFace(int v1, int v2, int v3) {
        int maxVert = Math.max(v1, Math.max(v2, v3));
        if (maxVert >= vertices.size())
            throw new IllegalArgumentException("Cannot create face with vertex out of range." +
                    "\nVertex added: number " + maxVert + " | Current number of vertices: " + vertices.size());

        int minVert = Math.min(v1, Math.min(v2, v3));
        if (minVert < 0)
            throw new IllegalArgumentException("Cannot create face with negative vertex index.");

        Face newFace = new Face(vertices.get(v1), vertices.get(v2), vertices.get(v3));
        if (!this.faces.contains(newFace)) this.faces.add(newFace);
        return this.faces.indexOf(newFace);
    }

    public int getFaceIndex(Face f) {
        return faces.indexOf(f);
    }

    public int getNumberOfFaces() {
        return faces.size();
    }

    protected int addMidPoint(Edge edge) {
        ColumnVector vp1 = vertexPositions.get(edge.getV1().getVertexPositionIndex());
        ColumnVector vp2 = vertexPositions.get(edge.getV2().getVertexPositionIndex());
        ColumnVector midPointPosition = vp1.add(vp2).scale(0.5f);
        int position = addVertexPosition(
                midPointPosition.getValue(0),
                midPointPosition.getValue(1),
                midPointPosition.getValue(2));

        ColumnVector vn1 = vertexNormals.get(edge.getV1().getVertexNormalIndex());
        ColumnVector vn2 = vertexNormals.get(edge.getV2().getVertexNormalIndex());
        ColumnVector midPointNormal = vn1.add(vn2).scale(0.5f);
        int normal = addVertexNormal(
                midPointNormal.getValue(0),
                midPointNormal.getValue(1),
                midPointNormal.getValue(2));

        return addVertex(new Vertex(position, normal));
    }

    private void convertToCartesianCoordinates() {
        if (coordinateType == CARTESIAN) return; //prevents unwanted double-conversion

        List<ColumnVector> cartesianCoordinates = new ArrayList<>();
        for (ColumnVector polar : vertexPositions) {
            cartesianCoordinates.add(Volume.convertPolarCoordinateToCartesian(polar));
        }
        vertexPositions = cartesianCoordinates;

        List<ColumnVector> cartesianNormals = new ArrayList<>();
        for (ColumnVector polar : vertexNormals) {
            cartesianNormals.add(Volume.convertPolarCoordinateToCartesian(polar));
        }
        vertexNormals = cartesianNormals;

        this.coordinateType = CARTESIAN;
    }

    private void convertToPolarCoordinates() {
        if (coordinateType == POLAR) return; //prevents unwanted double-conversion

        List<ColumnVector> cartesianPositions = new ArrayList<>();
        for (ColumnVector polar : vertexPositions) {
            cartesianPositions.add(Volume.convertCartesianCoordinateToPolar(polar));
        }
        vertexPositions = cartesianPositions;

        List<ColumnVector> cartesianNormals = new ArrayList<>();
        for (ColumnVector polar : vertexNormals) {
            cartesianNormals.add(Volume.convertCartesianCoordinateToPolar(polar));
        }
        vertexNormals = cartesianNormals;

        this.coordinateType = POLAR;
    }

    public void subdivideMesh() {
        Map<Edge, Integer> edgesToMidPointVertex = new HashMap<>();

        List<Face> oldFaces = new ArrayList<>(faces);
        faces.clear();
        for (Face face : oldFaces) {
            Vertex v0 = face.getV1();
            Vertex v1 = face.getV2();
            Vertex v2 = face.getV3();

            int v3 = edgesToMidPointVertex.computeIfAbsent(new Edge(v0, v1), this::addMidPoint);
            int v4 = edgesToMidPointVertex.computeIfAbsent(new Edge(v1, v2), this::addMidPoint);
            int v5 = edgesToMidPointVertex.computeIfAbsent(new Edge(v2, v0), this::addMidPoint);

            this.addFace(getVertexIndex(v0), v3, v5);
            this.addFace(getVertexIndex(v1), v4, v3);
            this.addFace(getVertexIndex(v2), v5, v4);
            this.addFace(v3, v4, v5);
        }
    }

    public List<Face> getFaces() {
        return new ArrayList<>(faces);
    }

    public Vertex getVertex(int i) {
        return vertices.get(i);
    }

    public Face getFace(int i) {
        return faces.get(i);
    }

    public int getDimensions() {
        return dimensions;
    }
}

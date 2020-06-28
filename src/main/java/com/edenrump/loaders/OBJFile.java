package com.edenrump.loaders;

import com.edenrump.math.arrays.ColumnVector;
import com.edenrump.math.shape.mesh.Face;
import com.edenrump.math.shape.textured.WrappedConstruct;
import com.edenrump.math.shape.textured.WrappedVertex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.edenrump.math.shape.mesh.GeometricConstruct.CARTESIAN;

public class OBJFile {

    private String[] vertexFileLines;
    private String[] textureFileLines;
    private String[] vertNormalsFileLines;
    private String[] faceLines;
    private final String fileName;

    private final WrappedConstruct mesh;

    public OBJFile(String fileName) {
        this.fileName = fileName;

        if (fileName == null) {
            throw new IllegalArgumentException("Cannot load OBJ file with null fileName");
        }

        parseTextByLineIdentifier(getReader());

        mesh = createMesh();
    }

    public WrappedConstruct getMesh() {
        return mesh;
    }

    private void parseTextByLineIdentifier(BufferedReader reader) {
        List<String> lines = reader.lines().collect(Collectors.toList());
        vertexFileLines = lines.stream().filter((line) -> line.startsWith("v ")).toArray(String[]::new);
        textureFileLines = lines.stream().filter((line) -> line.startsWith("vt ")).toArray(String[]::new);
        vertNormalsFileLines = lines.stream().filter((line) -> line.startsWith("vn ")).toArray(String[]::new);
        faceLines = lines.stream().filter((line) -> line.startsWith("f ")).toArray(String[]::new);
    }

    private WrappedConstruct createMesh() {
        WrappedConstruct construct = new WrappedConstruct(CARTESIAN);
        construct.setVertexPositions(getVertexPositions());
        construct.setVertexNormals(getVertexNormals());

        List<WrappedVertex> vertices = getVertices();
        List<ColumnVector> vertexTextureCoords = getTextureCoords();

        for (WrappedVertex v : vertices) {
            int vertexIndex = construct.addVertex(v);
            int textureCoordIndex = v.getVertexTextureCoordinate();
            ColumnVector vt = vertexTextureCoords.get(textureCoordIndex);
            construct.addVertexTextureCoordinate(vertexIndex, vt.getValue(0), vt.getValue(1));
        }

        for (Face face : getFaces()) {
            if (faceRefersToExistingVertices(face)) {
                int v1 = construct.getVertexIndex(face.getV1());
                int v2 = construct.getVertexIndex(face.getV2());
                int v3 = construct.getVertexIndex(face.getV3());
                construct.addFace(v1, v2, v3);
            } else {
                throw new RuntimeException("OBJ File " + fileName +
                        " is badly constructed: polygon face refers to vertex that does not exist");
            }
        }

        return construct;
    }

    private boolean faceRefersToExistingVertices(Face face) {
        WrappedVertex v1 = (WrappedVertex) face.getV1();
        WrappedVertex v2 = (WrappedVertex) face.getV2();
        WrappedVertex v3 = (WrappedVertex) face.getV3();

        int vp1 = v1.getVertexPositionIndex();
        int vp2 = v2.getVertexPositionIndex();
        int vp3 = v3.getVertexPositionIndex();
        int vn1 = v1.getVertexNormalIndex();
        int vn2 = v2.getVertexNormalIndex();
        int vn3 = v3.getVertexNormalIndex();
        int vt1 = v1.getVertexTextureCoordinate();
        int vt2 = v2.getVertexTextureCoordinate();
        int vt3 = v3.getVertexTextureCoordinate();

        boolean positions = Math.max(Math.max(vp1, vp2), vp3) < vertexFileLines.length &&
                Math.min(Math.min(vp1, vp2), vp3) >= 0;
        boolean normals = Math.max(Math.max(vn1, vn2), vn3) < vertNormalsFileLines.length &&
                Math.min(Math.min(vn1, vn2), vn3) >= 0;
        boolean textures = Math.max(Math.max(vt1, vt2), vt3) < textureFileLines.length &&
                Math.min(Math.min(vt1, vt2), vt3) >= 0;

        return positions && normals && textures;
    }

    private List<ColumnVector> getVertexPositions() {
        return parseMultipleLinesToFloatVectors(vertexFileLines, 3);
    }

    private List<ColumnVector> getTextureCoords() {
        return parseMultipleLinesToFloatVectors(textureFileLines, 2);
    }

    private List<ColumnVector> getVertexNormals() {
        return parseMultipleLinesToFloatVectors(vertNormalsFileLines, 3);
    }

    private List<Face> getFaces() {
        if (faceLines == null)
            return new ArrayList<>();

        List<Face> faces = new ArrayList<>();
        for (String faceLine : faceLines) {
            faces.add(createFaceFromOBJVertexNormalIndex(faceLine));
        }
        return faces;
    }

    private List<WrappedVertex> getVertices() {
        Set<WrappedVertex> vertexSet = new HashSet<>(); //ensures no duplicate vertices added
        for (String faceLine : faceLines) {
            String[] segments = segmentVertexNormalIndexLine(faceLine);
            vertexSet.add(createVertexFromVertexNormalIndexSegment(segments[1])); //skip 0, which is line type indicator
            vertexSet.add(createVertexFromVertexNormalIndexSegment(segments[2]));
            vertexSet.add(createVertexFromVertexNormalIndexSegment(segments[3]));
        }
        return new ArrayList<>(vertexSet);
    }

    private BufferedReader getReader() {
        FileReader isr;
        File objFile = new File(fileName);
        try {
            isr = new FileReader(objFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not load file: " + fileName);
        }

        return new BufferedReader(isr);
    }

    /**
     * Convert a string of format "f v1/vt1/vn1 v2/vt2/vn2 v3/vt3/vn3" into a triangle face
     *
     * @param line the OBJ line of the correct format
     * @return a face
     */
    private Face createFaceFromOBJVertexNormalIndex(String line) {
        String[] segments = segmentVertexNormalIndexLine(line);

        if (segments.length != 4)
            throw new RuntimeException("OBJ File " + fileName + " has not bee correctly triangulated." +
                    " Expected 3 verts per triangle, but found: " + segments.length);

        WrappedVertex v1 = createVertexFromVertexNormalIndexSegment(segments[1]); //skip 0, which is line type indicator
        WrappedVertex v2 = createVertexFromVertexNormalIndexSegment(segments[2]);
        WrappedVertex v3 = createVertexFromVertexNormalIndexSegment(segments[3]);

        return new Face(v1, v2, v3);
    }

    private String[] segmentVertexNormalIndexLine(String line) {
        String[] segments = line.split(" ");

        if (segments.length != 4) //three vertices plus line type indicator
            throw new RuntimeException("OBJ File " + fileName + " has not bee correctly triangulated." +
                    " Expected 3 vertices per face, found " + (segments.length - 1));

        return segments;
    }

    /**
     * Take a segment of format "v1/vt1/vn1" where:
     * v1 = the index of the vertex
     * vt1 = index of the texture coordinate
     * vn1 = index of the vertex normal
     *
     * @param segment a list of segments to parse
     * @return a vertex
     */
    private WrappedVertex createVertexFromVertexNormalIndexSegment(String segment) {
        String[] vertexInfo = segment.split("/");

        if (vertexInfo.length != 3)
            throw new RuntimeException("OBJ File " + fileName + " has not bee correctly triangulated." +
                    " Expected 3 indices for each face vertex but found " + (vertexInfo.length));

        int vertexIndex;
        int textureIndex;
        int normalIndex;
        try {
            vertexIndex = Integer.parseInt(vertexInfo[0]) - 1; //remove 1 because OBJ face format is 1-indexed not 0-indexed
            textureIndex = Integer.parseInt(vertexInfo[1]) - 1;
            normalIndex = Integer.parseInt(vertexInfo[2]) - 1;
        } catch (NumberFormatException nfe) {
            throw new RuntimeException("OBJ File " + fileName + " is badly formatted. Could not create vertex from polygonal face segment:" +
                    "\n>>>   " + segment + "   <<<");
        }
        return new WrappedVertex(vertexIndex, normalIndex, textureIndex);
    }

    private List<ColumnVector> parseMultipleLinesToFloatVectors(String[] lines, int expectedDimensions) {
        if (lines == null)
            return new ArrayList<>();

        List<ColumnVector> parsedData = new ArrayList<>();
        for (String line : lines) {
            ColumnVector data = parseLineToColumnVector(line);
            if (data.getDimensions() != expectedDimensions)
                throw new RuntimeException("Expected " + expectedDimensions + " dimensions, but got " +
                        data.getDimensions() + " in " + fileName);

            parsedData.add(data);
        }
        return parsedData;
    }

    private ColumnVector parseLineToColumnVector(String line) {
        String[] splitLine = line.split(" ");
        float[] values = new float[splitLine.length - 1];
        for (int i = 0; i < splitLine.length - 1; i++) {
            try {
                values[i] = Float.parseFloat(splitLine[i + 1]); //skip 0, which is line type indicator
            } catch (NumberFormatException e) {
                throw new RuntimeException("OBJ file " + fileName + " is badly formatted. Could not load geometric element on line:" +
                        "\n>>>   " + line + "   <<<" +
                        "\n as \"" + splitLine[i + 1] + "\" could not be read as a float");
            }
        }
        return new ColumnVector(values);
    }
}

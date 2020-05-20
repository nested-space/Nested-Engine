package com.edenrump.graphic.openGL_gpu;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.*;

/**
 * This class represents an attribute in OpenGL
 */
public class Attribute {

    public static final int INDEX_VERTEX_SIZE = 0x1;
    public static final int FLAT_COORDS_VERTEX_SIZE = 0x2;
    public static final int VOLUME_COORDS_VERTEX_SIZE = 0x3;

    public static final String POSITIONS_ATTRIB_NAME = "positions";
    public static final String TEXTURE_COORDS_ATTRIB_NAME = "textureCoordinates";
    public static final String COLOURS_ATTRIB_NAME = "colours";

    public static final int POSITION_COORDINATE_ATTRIB = 0x0;
    public static final int NORMALS_ATTRIB = 0x1;
    public static final int TEXTURE_COORDS_ATTRIB = 0x2;
    public static final int COLOURS_ATTRIB = 0x2;

    public static final int RAW_COORDINATES = 0x10;
    public static final int UNTEXTURED_MESH = 0x11;
    public static final int TEXTURED_MESH = 0x12;
    public static final int COLOURED_MESH = 0x13;


    /**
     * The attribute location.
     * <p>
     * This should generally be generated in the shader program.
     */
    private final int location;
    /**
     * The name of the attribute
     * <p>
     * This should be identical to that used by the shader program
     */
    private final String name;
    /**
     * Parameter representing number of bytes in between attribute information in GPU memory
     */
    private int stride;
    /**
     * Parameter representing number of bytes from beginning of VBO attribute starts in GPU memory
     */
    private int offset;
    /**
     * Parameter representing the size of the attribute
     */
    private int size;
    private int vboID;

    /**
     * Constructor allowing full set up of attribute for further use
     *
     * @param location the OpenGL handle for the attribute
     * @param name     the name of the attribute used in the Shader Program
     * @param size     the size of the attribute in bytes
     * @param stride   the stride between attribute values in GPU memory
     * @param offset   the gap between the start of the VBO and the start of the attribute in GPU memory
     */
    private Attribute(int location, String name, int size, int stride, int offset, int vboID) {
        this.location = location;
        this.name = name;
        this.size = size;
        this.stride = stride;
        this.offset = offset;
        this.vboID = vboID;
        pointVertexAttribute();
    }

    /**
     * Method to get type-safe attribute for referencing a standard location and
     * name for vertex positions
     */
    public static Attribute getDefault3DPositionsAttribute(int vboID) {
        return new Attribute(
                POSITION_COORDINATE_ATTRIB,
                POSITIONS_ATTRIB_NAME,
                3, 0, 0,
                vboID);
    }

    /**
     * Method to get type-safe attribute for referencing a standard location and
     * name for vertex positions
     */
    public static Attribute getDefault2DPositionsAttribute(int vboID) {
        return new Attribute(
                POSITION_COORDINATE_ATTRIB,
                POSITIONS_ATTRIB_NAME,
                2, 0, 0,
                vboID);
    }

    /**
     * Method to get type-safe attribute for referencing a standard location and
     * name for vertex texture coords
     */
    public static Attribute getDefaultTextureCoordsAttribute(int vboID) {
        return new Attribute(
                TEXTURE_COORDS_ATTRIB,
                TEXTURE_COORDS_ATTRIB_NAME,
                2, 0, 0,
                vboID);
    }

    public String getName() {
        return name;
    }

    /**
     * Method to get the size of the attribute in bytes
     *
     * @return size of attribute in bytes
     */
    public int getSize() {
        return size;
    }

    /**
     * Method to manually set size of attribute
     * <p>
     * WARNING: use with caution as will impact pointing and enabling this attribute.
     *
     * @param size the size of the attribute in bytes
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Method to get the stride between attribute values in GPU memory
     *
     * @return stride between attribute values in GPU memory
     */
    public int getStride() {
        return stride;
    }

    /**
     * Method to set stride between attribute values in GPU memory
     *
     * @param stride stride between attribute values in GPU memory
     */
    public void setStride(int stride) {
        this.stride = stride;
    }

    /**
     * Method to get the gap between the start of the VBO and the start of the attribute in GPU memory
     *
     * @return gap between the start of the VBO and the start of the attribute in GPU memory
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Method to set the gap between the start of the VBO and the start of the attribute in GPU memory
     *
     * @param offset gap between the start of the VBO and the start of the attribute in GPU memory
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * Method to get the location of the attribute
     *
     * @return the attribute location
     */
    public int getLocation() {
        return location;
    }

    public int getVBOId(){
        return vboID;
    }

    /**
     * Enables a vertex attribute.
     * TODO: stop this from being static
     */
    public void enableVertexAttribute() {
        glEnableVertexAttribArray(location);
    }

    /**
     * Disables a vertex attribute.
     */
    public void disableVertexAttribute() {
        glDisableVertexAttribArray(location);
    }

    /**
     * Method to set the size, stride and offset before pointing the attrbute in the GPU memory
     *
     * @param size   Number of values per vertex
     * @param stride Offset between consecutive generic vertex attributes in
     *               bytes
     * @param offset Offset of the first component of the first generic vertex
     *               attribute in bytes
     */
    public void pointVertexAttribute(int size, int stride, int offset) {
        setSize(size);
        setStride(stride);
        setOffset(offset);
        pointVertexAttribute();
    }

    /**
     * Method to point attribute in GPU memory using cached values of location, size, stride and offset
     */
    public void pointVertexAttribute() {
        glVertexAttribPointer(location, size, GL_FLOAT, false, stride, offset);
    }
}

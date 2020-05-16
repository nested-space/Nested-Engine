package com.edenrump.graphic.mesh;

/**
 * This utility class provides a typesafe way to store attributes in OpenGL
 *
 * @author Ed Eden-Rump
 */
public abstract class MeshVariables {

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


}

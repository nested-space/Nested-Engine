package com.edenrump.graphic.data;

/**
 * This class represents an attribute in OpenGL
 */
public class Attribute {

    /**
     * The attribute location.
     *
     * This should generally be generated in the shader program.
     */
    int attributeLocation;

    /**
     * The name of the attribute
     *
     * This should be identical to that used by the shader program
     */
    String attributeName;

    /**
     * Type-safe attribute for referencing a standard location and name for an index buffer
     */
    public static Attribute defaultIndicesAttribute = new Attribute(0, "indices");

    /**
     * Type-safe attribute for referencing a standard location and name for vertex positions
     */
    public static Attribute defaultPositionsAttribute = new Attribute(1, "positions");

    /**
     * Type-safe attribute for referencing a standard location and name for texture coordinates
     */
    public static Attribute defaultTextureCoordsAttribute = new Attribute(2, "textureCoordinates");

    /**
     * Constructor creates Attribute with location and name
     * @param location attribute location
     * @param name
     */
    public Attribute(int location, String name){
        this.attributeLocation = location;
        this.attributeName = name;
    }

    /**
     * Method to get the location of the attribute
     * @return the attribute location
     */
    public int getLocation() {
        return attributeLocation;
    }
}

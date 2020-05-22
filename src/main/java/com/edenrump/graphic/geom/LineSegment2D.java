package com.edenrump.graphic.geom;

import com.edenrump.math.arrays.ColumnVector;

/**
 * This class represents a line between two points.
 * <p>
 * It can be used to create a compound line with multiple segments.
 *
 * @author Ed Eden-Rump
 */
public class LineSegment2D {

    public static final float LINE_WIDTH_DEFAULT = 0.01f;
    public static final boolean SUBDIVIDED_DEFAULT = false;
    public static final float DEFAULT_SEGMENT_LENGTH = 0.01f;


    private boolean isSubDividedForAnimation = SUBDIVIDED_DEFAULT;
    private ColumnVector start;
    private ColumnVector end;
    private float lineWidth = LINE_WIDTH_DEFAULT;

    public LineSegment2D(ColumnVector start, ColumnVector end, float lineWidth, boolean isSubDividedForAnimation) {
        this.start = start;
        this.end = end;
        this.lineWidth = lineWidth;
        this.isSubDividedForAnimation = isSubDividedForAnimation;

        float[] mesh = createCacheMeshLineCoordinates(isSubDividedForAnimation, start, end);

        for(int i=0; i<mesh.length-1; i=i+2){
            System.out.println("Coordinate | x: " + mesh[i] + "\t\t\ty: " + mesh[i+1]);
        }
    }

    /**
     * This method sets the lineWidth in OpenGL coordinates (i.e. 1 is half the width or height of the screen)
     * <p>
     * TODO: normalise to ensure line's aren't wider than they are long, like the screen is! (i.e. remove coordinate bias)
     *
     * @param lineWidth the width of the line in OpenGL coordinates
     */
    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    /**
     * Creates a float array of vertex coordinates representing the triangles necessary to make up the line
     * <p>
     * If subdivided is set, will break line into segments of DEFAULT_SEGMENT_LENGTH before creating
     * coordinates.
     *
     * @param subdivideLine whether to divide the line into segments of DEFAULT_SEGMENT_LENGTH
     * @param start         the vertex coordinates of the middle of the start of the line (i.e. or a line with width 0)
     * @param end           the vertex coordinates of the middle of the end of the line (i.e. or a line with width 0)
     * @return the float array with all coordinates
     */
    private float[] createCacheMeshLineCoordinates(boolean subdivideLine, ColumnVector start, ColumnVector end) {
        float[] coordinates;
        if (subdivideLine) {
            //determine number of segments and initialise float array with correct size
            float length = start.getDistanceToOther(end);
            int numberOfCoordinates = Math.round(length / DEFAULT_SEGMENT_LENGTH) + 1;
            coordinates = new float[numberOfCoordinates * 4]; // multiply by 2 for x and y and for 2 because of cc and clockwise

            //break line into segments of length DEFAULT_SEGMENT_LENGTH
            float xDist = end.getValues()[0] - start.getValues()[0];
            float yDist = end.getValues()[1] - start.getValues()[1];
            ColumnVector[] lineCentreCoordinates = new ColumnVector[numberOfCoordinates];
            lineCentreCoordinates[0] = start;
            lineCentreCoordinates[lineCentreCoordinates.length - 1] = end;
            for (int i = 1; i < numberOfCoordinates - 1; i++) {    //only between second and penultimate as others set above
                float proportionTravelled = (float) i / (numberOfCoordinates - 1);
                lineCentreCoordinates[i] = new ColumnVector(
                        proportionTravelled * xDist,
                        proportionTravelled * yDist);
            }

            //create normalised direction vector for line
            ColumnVector ndv = new ColumnVector(xDist, yDist).normalize();

            //sew together meshes for segments, removing duplicates assuming rendering using GL_TRIANGLE_STRIPS
            for (int i = 0; i < lineCentreCoordinates.length; i++) {
                ColumnVector[] pprCoords = getPerpendicularCoordinate(
                        ndv,
                        lineCentreCoordinates[i]);
                coordinates[4*i] =      pprCoords[0].getValues()[0];        //ccVec
                coordinates[4*i+1] =    pprCoords[0].getValues()[1];
                coordinates[4*i+2] =    pprCoords[1].getValues()[0];        //cVec
                coordinates[4*i+3] =    pprCoords[1].getValues()[1];
            }
        } else {
            coordinates = new float[8];

            //calculate the unit vector for direction of line

            //normalise vector

            //scale to lineWidth

            //create 2 new vectors - v1 one rotated by 90 degrees and v2 rotated anticlockwise by 90 degrees

            //create 2 new vectors by translating the previous 2 vectors by r (line distance) v3 and v4

            /*sew together float array in order:
             *   1. v1
             *   2. v4
             *   3. v2 -- triangle 1 in counter clockwise order
             *   4. v3 -- next triangle added automatically as GL_TRIANGLE_STRIPS
             */


        }
        return coordinates;
    }

    /**
     * Method to take the direction of the line and generate two points either side of the
     * line, centred on the current2DCoordinate of the vector supplied, which will act as mesh vertex
     * coordinates for the line segment
     *
     * @param unitDirectionVector a unit vector with direction of line
     * @param current2DCoordinate xy coord point at which to determine coords
     * @return ColumnVector[2] with counter-clockwise coordinates first (pos 0) at distance lineWidth(relative to vector direction)
     */
    private ColumnVector[] getPerpendicularCoordinate(ColumnVector unitDirectionVector, ColumnVector current2DCoordinate) {
        ColumnVector[] coordinates = new ColumnVector[2];

        //scale unit vector by lineWidth
        ColumnVector scaledVector = unitDirectionVector.scale(lineWidth);

        //create 2 vectors - one for clockwise and one for counter-clockwise coords
        ColumnVector cVec = new ColumnVector(scaledVector.getValues()[1], -scaledVector.getValues()[0]);
        ColumnVector ccVec = new ColumnVector(-scaledVector.getValues()[1], scaledVector.getValues()[0]);

        //get coordinate rotated 90 degrees counter clockwise
        coordinates[0] = current2DCoordinate.add(ccVec);
        coordinates[1] = current2DCoordinate.add(cVec);

        return coordinates;
    }

    /**
     * Gets the start coordinate of the line segment
     * @return coordinates of line beginning
     */
    public ColumnVector getStart() {
        return start;
    }

    /**
     * Gets the end coordinate of the line segment
     * @return coordinates of line end
     */
    public ColumnVector getEnd() {
        return end;
    }

    /**
     * Gets the width of the line to be plotted - remember this is in OpenGL coordinates,
     * so relative to size of screen, therefore numbers should be small
     *
     * @return width of line
     */
    public float getLineWidth() {
        return lineWidth;
    }


}
package com.edenrump.graphic.viewport.display;

import java.text.DecimalFormat;

public class Bounds {

    float minX, maxX, minY, maxY;

    public Bounds(float minX, float maxX, float minY, float maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public float getMinX() {
        return minX;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMinY() {
        return minY;
    }

    public float getMaxY() {
        return maxY;
    }

    public void setMinX(float x) {
        if (x < this.maxX) {
            this.minX = x;
        } else {
            this.minX = maxX;
            this.maxX = x;
        }
    }

    public void setMaxX(float x) {
        if (x > this.minX) {
            this.maxX = x;
        } else {
            this.minX = x;
            this.maxX = x;
        }
    }

    public void setMinY(float y) {
        if (y < this.maxY) {
            this.minY = y;
        } else {
            this.minY = maxY;
            this.maxY = y;
        }
    }

    public void setMaxY(float y) {
        if (y > this.minY) {
            this.maxY = y;
        } else {
            this.minY = y;
            this.maxY = y;
        }
    }

    /**
     * Return whether this bounding box completely contains the specified bounding box
     *
     * @param other the other bounding box
     * @return whether this completely contains the other box
     */
    public boolean contains(Bounds other) {
        return this.maxX > other.maxX &&
                this.minX < other.minX &&
                this.maxY > other.maxY &&
                this.minY < other.minY;
    }

    /**
     * Return whether this bounding box intersects with the specified bounding box
     *
     * @param other a bounding box
     * @return whether this intersects with the specified box
     */
    public boolean intersects(Bounds other) {
        return this.minX < other.maxX &&
                this.maxX > other.minX &&
                this.minY < other.maxY &&
                this.maxY > other.minY;
    }

    /**
     * Return whether one or both dimensions
     *
     * @return true if either dimensions is of zero length
     */
    public boolean isEmpty() {
        float THRESHOLD = 0.000001f;
        return (maxX - minX < THRESHOLD) || (maxY - minY < THRESHOLD);
    }

    @Override
    public String toString() {
        DecimalFormat format = new DecimalFormat("###.##");
        return "Bounds (MinX: " + format.format(minX) +
                ", MaxX: " + format.format(maxX) +
                ", MinY: " + format.format(minY) +
                ", MaxY: " + format.format(maxY);
    }

    @Override
    public boolean equals(Object obj) {
        float THRESHOLD = 0.000001f;
        if (obj instanceof Bounds) {
            Bounds other = (Bounds) obj;
            return Math.abs(this.minX - other.minX) < THRESHOLD &&
                    Math.abs(this.minY - other.minY) < THRESHOLD &&
                    Math.abs(this.maxX - other.maxX) < THRESHOLD &&
                    Math.abs(this.maxY - other.maxY) < THRESHOLD;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Math.round(minX * 1163 +
                maxX * 7879 +
                minY * 2687 +
                maxY + 7109);
    }

    public float getWidth() {
        return maxX - minX;
    }

    public float getHeight() {
        return maxY - minY;
    }
}

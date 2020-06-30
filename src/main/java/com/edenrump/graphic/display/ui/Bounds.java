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

package com.edenrump.graphic.display.ui;

import java.text.DecimalFormat;

public class Bounds {

    private final float minX, maxX, minY, maxY;

    public Bounds(float minX, float minY, float width, float height) {
        this.minX = minX;
        this.maxX = minX + width;
        this.minY = minY;
        this.maxY = minY + height;
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

package com.edenrump.math.arrays;

/*
 * The MIT License (MIT)
 *
 * Copyright © 2015-2017, Heiko Brumme
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.nio.FloatBuffer;

public class Vector2f {

    final float x;
    final float y;

    /**
     * Creates a default 2-tuple vector with all values set to 0.
     */
    public Vector2f() {
        this.x = 0f;
        this.y = 0f;
    }

    /**
     * Creates a 2-tuple vector with specified values.
     *
     * @param x x value
     * @param y y value
     */
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Normalizes the vector.
     *
     * @return Normalized vector
     */
    public Vector2f normalize() {
        return divide(length());
    }

    /**
     * Negates this vector.
     *
     * @return Negated vector
     */
    public Vector2f negate() {
        return scale(-1f);
    }

    /**
     * Calculates the length of the vector.
     *
     * @return Length of this vector
     */
    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    /**
     * Calculates the squared length of the vector.
     *
     * @return Squared length of this vector
     */
    public float lengthSquared() {
        return x * x + y * y;
    }

    /**
     * Adds this vector to another vector.
     *
     * @param other The other vector
     * @return Sum of this + other
     */
    public Vector2f add(Vector2f other) {
        float x = this.x + other.x;
        float y = this.y + other.y;
        return new Vector2f(x, y);
    }

    /**
     * Subtracts this vector from another vector.
     *
     * @param other The other vector
     * @return Difference of this - other
     */
    public Vector2f subtract(Vector2f other) {
        return this.add(other.negate());
    }

    /**
     * Multiplies a vector by a scalar.
     *
     * @param scalar Scalar to multiply
     * @return Scalar product of this * scalar
     */
    public Vector2f scale(float scalar) {
        float x = this.x * scalar;
        float y = this.y * scalar;
        return new Vector2f(x, y);
    }

    /**
     * Divides a vector by a scalar.
     *
     * @param scalar Scalar to multiply
     * @return Scalar quotient of this / scalar
     */
    public Vector2f divide(float scalar) {
        if(scalar==0) throw new ArithmeticException("Cannot properly divide by 0");
        return scale(1f / scalar);
    }

    /**
     * Calculates the dot product of this vector with another vector.
     *
     * @param other The other vector
     * @return Dot product of this * other
     */
    public float dot(Vector2f other) {
        return this.x * other.x + this.y * other.y;
    }

    /**
     * Calculates a linear interpolation between this vector with another
     * vector.
     *
     * @param other The other vector
     * @param alpha The alpha value, must be between 0.0 and 1.0
     * @return Linear interpolated vector
     */
    public Vector2f lerp(Vector2f other, float alpha) {
        return this.scale(1f - alpha).add(other.scale(alpha));
    }

    /**
     * Stores the vector in a given Buffer.
     *
     * @param buffer The buffer to store the vector data
     */
    public void storeCoordinatesInBuffer(FloatBuffer buffer) {
        buffer.put(x).put(y);
        buffer.flip();
    }

    public float getSquareDistanceToOther(Vector2f other){
        return (other.x - this.x) * (other.x - this.x) + (other.y - this.y) * (other.y - this.y);
    }

    public float getDistanceToOther(Vector2f other) {
        return (float) Math.sqrt(getSquareDistanceToOther(other));
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public int hashCode() {
        return (int) x * 17 + (int) y * 13;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector2f) {
            Vector2f other = (Vector2f) obj;
            return x == other.getX() && y == other.getY();
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "Vector: (" + x + "," + y + ")";
    }
}
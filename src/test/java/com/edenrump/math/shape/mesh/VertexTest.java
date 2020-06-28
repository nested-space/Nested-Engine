package com.edenrump.math.shape.mesh;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Random;

public class VertexTest {

    final Random r = new Random(10258);
    final Vertex v1 = new Vertex(r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE));
    final Vertex v2 = new Vertex(r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE));
    final Vertex v3 = new Vertex(r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE));
    final Vertex v4 = new Vertex(r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE));

    @Test
    public void constructorTest() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new Vertex(-1, r.nextInt(Integer.MAX_VALUE)));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Vertex(r.nextInt(Integer.MAX_VALUE), -1));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Vertex(-1, -1));
    }

    @Test
    public void equalsTest() {
        Vertex v5 = new Vertex(v1.getVertexPositionIndex(), v1.getVertexNormalIndex());
        Assert.assertEquals(v1, v1);
        Assert.assertEquals(v1, v5);
        Assert.assertEquals(v3, v3);
        Assert.assertNotEquals(v1, v3);
        Assert.assertNotEquals(v2, v4);
    }

    @Test
    public void hashcodeTest() {
        Assert.assertEquals(v1.hashCode(), v1.hashCode());
        Assert.assertEquals(v2.hashCode(), v2.hashCode());
        Assert.assertNotEquals(v1.hashCode(), v2.hashCode());
        Assert.assertNotEquals(v3.hashCode(), v4.hashCode());
    }

}

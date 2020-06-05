package com.edenrump.math.shape.mesh;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Random;

public class FaceTest {

    Random r = new Random(10258);
    Vertex v1 = new Vertex(r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE));
    Vertex v2 = new Vertex(r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE));
    Vertex v3 = new Vertex(r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE));

    @Test
    public void constructTest() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new Face(v1, v1, v2));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Face(null, v1, v2));
    }

    @Test
    public void equalsTest(){
        Assert.assertEquals(new Face(v1, v2, v3), new Face(v1, v2, v3));
        Assert.assertNotEquals(new Face(v1, v2, v3), new Face(v2, v1, v3));
    }

    @Test
    public void hashcodeTest(){
        Assert.assertEquals(new Face(v1, v2, v3).hashCode(), new Face(v1, v2, v3).hashCode());
    }
}

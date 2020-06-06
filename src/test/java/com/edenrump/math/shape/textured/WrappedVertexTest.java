package com.edenrump.math.shape.textured;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Random;

public class WrappedVertexTest {

    Random r = new Random(10258);
    WrappedVertex wv1 =
            new WrappedVertex(r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE));
    WrappedVertex wv1a =
            new WrappedVertex(wv1.getVertexPositionIndex(), wv1.getVertexNormalIndex(), wv1.getVertexTextureCoordinate());
    WrappedVertex wv2 =
            new WrappedVertex(r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE));
    WrappedVertex wv3 =
            new WrappedVertex(r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE));

    @Test
    public void constructTest() {
        Assert.assertThrows(IllegalArgumentException.class, () ->
                new WrappedVertex(1, 1, -55));
    }

    @Test
    public void equalsTest() {
        Assert.assertEquals(wv1, wv1a);
        Assert.assertNotEquals(wv1, wv3);
    }

    @Test
    public void haschodeTest() {
        Assert.assertEquals(wv1.hashCode(), wv1.hashCode());
        Assert.assertEquals(wv2.hashCode(), wv2.hashCode());
        Assert.assertNotEquals(wv1.hashCode(), wv2.hashCode());
        Assert.assertNotEquals(wv2.hashCode(), wv3.hashCode());
    }
}

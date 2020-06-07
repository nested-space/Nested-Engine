package com.edenrump.math.shape.mesh;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EdgeTest {

    Random r = new Random(10258);
    Vertex v1 = new Vertex(r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE));
    Vertex v2 = new Vertex(r.nextInt(Integer.MAX_VALUE), r.nextInt(Integer.MAX_VALUE));
    Edge edge1 = new Edge(v1, v2);
    Edge edge2 = new Edge(v1, v2);

    @Test
    public void constructorTest() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new Edge(null, new Vertex(1, 1)));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Edge(new Vertex(1, 1), null));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Edge(null, null));
    }

    @Test
    public void equalsTest() {
        Assert.assertEquals(edge1, edge2);
        Assert.assertNotEquals(edge1, new Edge(v2, v1));
    }

    @Test
    public void hashcodeTest() {
        Assert.assertEquals(edge1.hashCode(), edge2.hashCode());
    }

    @Test
    public void mapTest(){
        Map<Edge, Integer> edges = new HashMap<>();
        edges.put(edge1, 1);
        edges.put(edge2, 2);
        Assert.assertTrue(edges.containsKey(edge1));
        Assert.assertTrue(edges.containsKey(edge2));

        Edge backwardsEdge = new Edge(edge1.v2, edge1.v1);
        Assert.assertTrue(edges.containsKey(backwardsEdge));
    }

}

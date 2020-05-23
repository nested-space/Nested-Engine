package com.edenrump.graphic.mesh;

import com.edenrump.graphic.openGL_gpu.Attribute;
import com.edenrump.graphic.openGL_gpu.VertexArrayObject;

import java.util.ArrayList;
import java.util.List;

public abstract class Mesh {

    private final VertexArrayObject vao;
    private final List<Attribute> attributes = new ArrayList<>();

    Mesh() {
        vao = new VertexArrayObject();
    }

    VertexArrayObject getVao() {
        return vao;
    }

    List<Attribute> getAttributes() {
        return attributes;
    }

    Attribute getAttribute(String attributeName) {
        for (Attribute attribute : attributes) {
            if (attribute.getName().equals(attributeName)) return attribute;
        }
        return null;
    }

    void associateAttribute(Attribute attribute) {
        attributes.add(attribute);
    }
}

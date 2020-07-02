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

package com.edenrump.graphic.shaders;

import com.edenrump.gpu.objects.Uniform;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20C.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20C.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL31.glGetUniformBlockIndex;
import static org.lwjgl.opengl.GL31.glUniformBlockBinding;

/**
 * This class represents a shader program.
 *
 * @author Ed Eden-Rump
 * @project Nested Engine
 */
public class ShaderProgram {

    /**
     * Stores the handle of the program.
     */
    private final int id;

    private Map<CharSequence, Uniform> uniformLocationMap;

    public ShaderProgram() {
        id = glCreateProgram();
    }

    public static ShaderProgram simpleTextureShaderProgram() {
        //files required for this shader
        final String VERTEX_FILE_LOCATION = "src/resources/shaderCode/flat_texture.vert";
        final String FRAGMENT_FILE_LOCATION = "src/resources/shaderCode/flat_texture.frag";

        Shader v = Shader.loadShader(GL_VERTEX_SHADER, VERTEX_FILE_LOCATION);
        Shader f = Shader.loadShader(GL_FRAGMENT_SHADER, FRAGMENT_FILE_LOCATION);
        ShaderProgram entityShaderProgram = new ShaderProgram();
        entityShaderProgram.attachShaders(v, f);
        entityShaderProgram.link();
        v.delete();
        f.delete();

        return entityShaderProgram;
    }

    public void bindUniformBlock(String blockName, int bufferBlockBinding) {
        glUseProgram(id);
        glUniformBlockBinding(
                id,
                glGetUniformBlockIndex(id, blockName),
                bufferBlockBinding);
        glUseProgram(0);
    }

    public void attachShader(Shader shader) {
        glAttachShader(id, shader.getID());
    }

    public void attachShaders(Shader... shaders) {
        for (Shader shader : shaders) {
            attachShader(shader);
        }
    }

    public void link() {
        glLinkProgram(id);
        checkStatus();
    }

    public int getAttributeLocation(CharSequence name) {
        return glGetAttribLocation(id, name);
    }

    public void addUniforms(String[] uniforms) {
        if (uniformLocationMap == null) uniformLocationMap = new HashMap<>();

        use();
        for (String uniformName : uniforms) {
            int location = glGetUniformLocation(id, uniformName);

            if (location == -1)
                throw new RuntimeException("Shader Program " + id + " does not contain uniform " + uniformName);

            Uniform uniform = new Uniform(id, location, uniformName);
            uniformLocationMap.put(uniformName, uniform);
        }
    }

    public Uniform getUniform(CharSequence name) {
        if (uniformLocationMap == null) uniformLocationMap = new HashMap<>();

        if (uniformLocationMap.get(name) == null)
            uniformLocationMap.put(name, new Uniform(id, getUniformLocation(name), name));

        return uniformLocationMap.get(name);
    }

    private int getUniformLocation(CharSequence name){
        use();
        return glGetUniformLocation(id, name);
    }

    public void use() {
        glUseProgram(id);
    }

    private void checkStatus() {
        int status = glGetProgrami(id, GL_LINK_STATUS);
        if (status != GL_TRUE) {
            throw new RuntimeException(glGetProgramInfoLog(id));
        }
    }

    public void delete() {
        glDeleteProgram(id);
    }
}

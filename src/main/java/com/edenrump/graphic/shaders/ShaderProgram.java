/*
 * The MIT License (MIT)
 *
 * Copyright © 2014-2017, Heiko Brumme
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
package com.edenrump.graphic.shaders;

import com.edenrump.graphic.gpu.Uniform;

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

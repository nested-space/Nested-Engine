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

import java.io.*;

import static org.lwjgl.opengl.GL20.*;

/**
 * This class represents a shader program.
 *
 * @author Ed Eden-Rump
 */
public class Shader {

    public static final int VERTEX = GL_VERTEX_SHADER;
    public static final int FRAGMENT = GL_FRAGMENT_SHADER;

    /**
     * Stores the handle of the shader.
     */
    private final int id;

    /**
     * Creates a shader with specified type.
     *
     * @param type Type of the shader
     */
    public Shader(int type) {
        id = glCreateShader(type);
    }

    /**
     * Creates a shader with specified type and source and compiles it.
     *
     * @param type   Type of the shader
     * @param source Source of the shader
     * @return Compiled Shader from the specified source
     */
    public static Shader createShader(int type, CharSequence source) {
        Shader shader = new Shader(type);
        shader.source(source);
        shader.compile();

        return shader;
    }

    /**
     * Loads a shader from a file.
     *
     * @param type Type of the shader
     * @param path File path of the shader
     * @return Compiled Shader from specified file
     */
    public static Shader loadShader(int type, String path) {
        File file = new File(path);
        if (!file.exists())
            throw new IllegalStateException("no file at: " + file.getAbsolutePath());

        StringBuilder builder = new StringBuilder();

        try (InputStream in = new FileInputStream(path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load a shader file!"
                    + System.lineSeparator() + ex.getMessage());
        }
        CharSequence source = builder.toString();

        return createShader(type, source);
    }

    /**
     * Sets the source code of this shader.
     *
     * @param source GLSL Source Code for the shader
     */
    public void source(CharSequence source) {
        glShaderSource(id, source);
    }

    /**
     * Compiles the shader and checks it's status afterwards.
     */
    public void compile() {
        glCompileShader(id);

        checkStatus();
    }

    /**
     * Checks if the shader was compiled successfully.
     */
    private void checkStatus() {
        int status = glGetShaderi(id, GL_COMPILE_STATUS);
        if (status != GL_TRUE) {
            throw new RuntimeException(glGetShaderInfoLog(id));
        }
    }

    /**
     * Deletes the shader.
     */
    public void delete() {
        glDeleteShader(id);
    }

    /**
     * Getter for the shader ID.
     *
     * @return Handle of this shader
     */
    public int getID() {
        return id;
    }

}

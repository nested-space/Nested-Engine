/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2019 Ed Eden-Rump
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in all
 *  * copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  * SOFTWARE.
 *
 */
package com.edenrump.graphic.shaders;

import static org.lwjgl.opengl.GL20C.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20C.GL_VERTEX_SHADER;

public class StaticEntityShaderProgram extends ShaderProgram {

    private static final String VERTEX_FILE_LOCATION = "res/shaderCode/flat_texture.vert";
    private static final String FRAGMENE_FILE_LOCATION = "res/shaderCode/flat_texture.frag";
    private static final String POSITION_ATTR_NAME = "position";
    private static final String TEXTURE_COORDS_ATTR_NAME = "textureCoordinates";
    private ShaderProgram program;
    private int positionsLocation;
    private int textureCoordinatesLocation;
    public StaticEntityShaderProgram() {
        Shader v = Shader.loadShader(GL_VERTEX_SHADER, VERTEX_FILE_LOCATION);
        Shader f = Shader.loadShader(GL_FRAGMENT_SHADER, FRAGMENE_FILE_LOCATION);

        program = new ShaderProgram();
        program.attachShaders(v, f);
        program.link();

        v.delete();
        f.delete();

        positionsLocation = program.getAttributeLocation(POSITION_ATTR_NAME);
        textureCoordinatesLocation = program.getAttributeLocation(TEXTURE_COORDS_ATTR_NAME);
    }
}

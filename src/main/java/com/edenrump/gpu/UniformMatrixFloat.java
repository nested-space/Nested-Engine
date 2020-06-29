package com.edenrump.gpu;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20C.*;

/*
 * Copyright (c) 2020 Ed Eden-Rump
 *     This file is part of Nested Engine.
 *
 *     Nested Engine is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Nested Engine is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Nested Engine.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     @Author Ed Eden-Rump
 *
 */

public class UniformMatrixFloat extends Uniform {

    public UniformMatrixFloat(int shaderProgramID, int location, CharSequence name) {
        super(shaderProgramID, location, name);
    }

    public void update_2x2(FloatBuffer buffer){
        if(buffer.remaining() < 4)
            throw new RuntimeException("Buffer of incorrect size to update Uniform Matrix of 2x2");
        engageShader();
        glUniformMatrix2fv(getLocation(), false, buffer);
    }

    public void update_3x3(FloatBuffer buffer){
        if(buffer.remaining() < 9)
            throw new RuntimeException("Buffer of incorrect size to update Uniform Matrix of 3x3");
        engageShader();
        glUniformMatrix3fv(getLocation(), false, buffer);
    }

    public void update_4x4(FloatBuffer buffer){
        if(buffer.remaining() < 16)
            throw new RuntimeException("Buffer of incorrect size to update Uniform Matrix of 4x4");
        engageShader();
        glUniformMatrix4fv(getLocation(), false, buffer);
    }
}

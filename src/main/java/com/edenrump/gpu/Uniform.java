package com.edenrump.gpu;

import static org.lwjgl.opengl.GL20.glUseProgram;

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

public class Uniform {

    final int location;
    final int shaderProgramID;
    final CharSequence name;

    public Uniform(int shaderProgramID, int location, CharSequence name) {
        this.shaderProgramID = shaderProgramID;
        this.location = location;
        this.name = name;
    }

    public CharSequence getName() {
        return name;
    }

    public int getShaderProgramID() {
        return shaderProgramID;
    }

    public int getLocation() {
        return location;
    }

    void engageShader() {
        glUseProgram(shaderProgramID);
    }

    void disengageShader() {
        glUseProgram(0);
    }

    public UniformFloat asUniformFloat() {
        return new UniformFloat(shaderProgramID, location, name);
    }

    public UniformInt asUniformInt() {
        return new UniformInt(shaderProgramID, location, name);
    }

    public UniformMatrixFloat asUniformMatrix() {
        return new UniformMatrixFloat(shaderProgramID, location, name);
    }

}

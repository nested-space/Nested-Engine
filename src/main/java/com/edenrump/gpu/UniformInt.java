package com.edenrump.gpu;

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

public class UniformInt extends Uniform {

    public UniformInt(int shaderProgramID, int location, CharSequence name) {
        super(shaderProgramID, location, name);
    }

    public void update(int v1) {
        engageShader();
        glUniform1i(getLocation(), v1);
    }

    public void update2values(int v1, int v2) {
        engageShader();
        glUniform2i(getLocation(), v1, v2);
    }

    public void update3values(int v1, int v2, int v3) {
        engageShader();
        glUniform3i(getLocation(), v1, v2, v3);
    }

    public void update4values(int v1, int v2, int v3, int v4) {
        engageShader();
        glUniform4i(getLocation(), v1, v2, v3, v4);
    }
}

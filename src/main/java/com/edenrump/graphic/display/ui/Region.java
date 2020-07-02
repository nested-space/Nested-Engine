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

package com.edenrump.graphic.display.ui;

/**
 * @author Ed Eden-Rump
 * @created 02/07/2020 - 16:45
 * @project Nested Engine
 **/
public class Region {

    PositionConstraint x;
    PositionConstraint y;
    LengthConstraint w;
    LengthConstraint h;

    public PositionConstraint getX() {
        return x;
    }

    public void setX(PositionConstraint x) {
        this.x = x;
    }

    public PositionConstraint getY() {
        return y;
    }

    public void setY(PositionConstraint y) {
        this.y = y;
    }

    public LengthConstraint getW() {
        return w;
    }

    public void setW(LengthConstraint w) {
        this.w = w;
    }

    public LengthConstraint getH() {
        return h;
    }

    public void setH(LengthConstraint h) {
        this.h = h;
    }
}

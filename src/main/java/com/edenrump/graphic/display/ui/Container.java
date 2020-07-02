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

import com.edenrump.math.util.Scalar;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ed Eden-Rump
 * @created 30/06/2020 - 17:54
 * @project Nested Engine
 **/
public abstract class Container extends Region {

    List<Component> components = new ArrayList<>();
    SizingBehaviour sizingBehaviour;

    protected Container() {
        sizingBehaviour = new SizingBehaviour(this);
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public void addComponent(int index, Component component) {
        index = Scalar.clamp(index, 0, components.size());
        components.add(index, component);
    }

    public void addComponents(List<Component> components) {
        this.components.addAll(components);
    }

    public void removeComponent(Component component) {
        components.remove(component);
    }

    public void clearComponents() {
        components.clear();
    }
}

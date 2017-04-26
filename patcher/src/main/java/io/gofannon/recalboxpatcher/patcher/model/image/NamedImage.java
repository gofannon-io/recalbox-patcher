/*
 * Copyright 2017  the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.gofannon.recalboxpatcher.patcher.model.image;

import javafx.scene.image.Image;

import static org.apache.commons.lang3.Validate.*;

/**
 * Just a JavaFX {@link javafx.scene.image.Image image} with a name.
 * <p>The name is used as a key and for filename (without extension) if necessary</p>
 */
public class NamedImage {

    private String name;
    private Image image;

    public NamedImage(NamedImage copy) {
        notNull(copy,"copy argument cannot be null");
        this.name = copy.name;
        this.image = copy.image;
    }

    public NamedImage(String name, Image image) {
        setName(name);
        setImage(image);
    }

    public String getName() {
        return name;
    }

    public final void setName(String name) {
        notEmpty(name, "name argument shall not be empty");
        this.name = name;
    }

    public Image getImage() {
        return image;
    }

    public final void setImage(Image image) {
        notNull(image, "image argument shall not be null");
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NamedImage that = (NamedImage) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
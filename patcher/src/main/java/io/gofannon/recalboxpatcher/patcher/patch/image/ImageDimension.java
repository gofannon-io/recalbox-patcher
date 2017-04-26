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

package io.gofannon.recalboxpatcher.patcher.patch.image;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * Dimension of an image.
 */
public class ImageDimension {

    private int width;
    private int height;

    public ImageDimension() {
    }

    public ImageDimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public ImageDimension(ImageDimension dimension) {
        notNull(dimension,"dimension argument shall not be null");
        this.width = dimension.width;
        this.height = dimension.height;
    }


    public int getWidth() {
        return width;
    }

    public final void setWidth(int width) {
        isTrue(width>=0, "width argument shall be equal or greater than 0");
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public final void setHeight(int height) {
        isTrue(height>=0, "height argument shall be equal or greater than 0");
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageDimension that = (ImageDimension) o;

        if (width != that.width) return false;
        return height == that.height;
    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + height;
        return result;
    }
}

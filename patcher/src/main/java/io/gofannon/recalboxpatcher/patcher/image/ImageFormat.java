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

package io.gofannon.recalboxpatcher.patcher.image;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;


public enum ImageFormat {

    JPEG(BufferedImage.TYPE_3BYTE_BGR, "jpeg", "jpg", "jpeg"),
    PNG(BufferedImage.TYPE_INT_ARGB, "png", "png"),
    GIF(BufferedImage.TYPE_INT_ARGB, "gif", "gif");


    private int type;
    private String imageIOType;
    private List<String> extensions = new ArrayList<>();

    private ImageFormat(int type, String imageIOType, String... extensions) {
        this.type = type;
        this.imageIOType = imageIOType;
        this.extensions.addAll(asList(extensions));
    }

    public int getType() {
        return type;
    }


    public String getImageIOFormatName() {
        return imageIOType;
    }


    public List<String> getExtensions() {
        return extensions;
    }


    public String getDefaultExtension() {
        return extensions.get(0);
    }

    public boolean isCompatibleExtension(String extension) {
        if (extension == null)
            return false;
        return extensions.contains(extension.toLowerCase());
    }

    public static boolean isSupportedExtension(String extension) {
        if (extension == null)
            return false;

        for (ImageFormat format : ImageFormat.values()) {
            if (format.isCompatibleExtension(extension))
                return true;
        }

        return false;
    }

    public static ImageFormat fromExtension(String extension) {
        if (extension == null)
            return null;

        for (ImageFormat format : ImageFormat.values()) {
            if( format.isCompatibleExtension(extension))
                return format;
        }

        return null;
    }
}
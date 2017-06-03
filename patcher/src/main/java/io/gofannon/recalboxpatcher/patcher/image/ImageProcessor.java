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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static org.apache.commons.lang3.Validate.*;

public class ImageProcessor {

    private boolean resize = false;
    private int width;
    private int height;

    private boolean changeFormat;
    private String format;


    public ImageProcessor resize(int newWidth, int newHeight) {
        isValidDimension("width", newWidth);
        isValidDimension("height", newHeight);

        this.width = newWidth;
        this.height = newHeight;
        this.resize = true;

        return this;
    }

    private void isValidDimension(String name, int dim) {
        isTrue(dim > 0, "%s %d is not greater than 0", name, dim);
        isTrue(dim <=500, "%s %d is not lower or equal to 500", name, dim);
    }

    public ImageProcessor changeFormat(String newFormat) {
        isTrue(isSupportedFormat(newFormat), "Unsupported image format %s", newFormat);

        changeFormat = true;
        this.format = newFormat;

        return this;
    }

    private static boolean isSupportedFormat(String newFormat) {
        if (newFormat == null)
            return false;
        switch (newFormat.toLowerCase()) {
            case "jpg":
            case "png":
            case "gif":
                return true;
            default:
                return false;
        }
    }

    public BufferedImage process(InputStream sourceImageInputStream) throws IOException {
        BufferedImage sourceImage = ImageIO.read(sourceImageInputStream);

        BufferedImage resizedImage = resizeImage(sourceImage);
        BufferedImage newFormatImage = changeFormat(resizedImage);
        return newFormatImage;
    }

    private BufferedImage resizeImage(BufferedImage sourceImage) {
        if( this.resize==false || hasAlreadyExpectedSize(sourceImage))
            return sourceImage;

        Image resizedImage = sourceImage.getScaledInstance(this.width, this.height, Image.SCALE_SMOOTH);
        return toBufferedImage(resizedImage);
    }

    private boolean hasAlreadyExpectedSize(BufferedImage sourceImage) {
        return sourceImage.getWidth(null)==this.width
                && sourceImage.getHeight(null)==this.height;
    }


    private static BufferedImage toBufferedImage(Image source) {
        BufferedImage bufferedImage = new BufferedImage(
                source.getWidth(null),
                source.getHeight(null),
                BufferedImage.TYPE_INT_RGB);
        bufferedImage.getGraphics().drawImage(source, 0, 0, null);
        return bufferedImage;
    }

    private BufferedImage changeFormat(BufferedImage sourceImage) {
        return sourceImage;
    }
}
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

package io.gofannon.recalboxpatcher.patcher.utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.apache.commons.io.FileUtils.*;
import static org.apache.commons.lang3.Validate.*;

public final class ImageUtils {

    /**
     * Load a JavaFX image from a file
     * @param file a non null image file
     * @return a non null image
     * @throws IOException if the image cannot be loaded
     */
    public static Image loadImage(File file ) throws IOException {
        notNull(file,"file argument shall not be null");

        try(InputStream in = openInputStream(file)) {
            return new Image(in);
        }
    }

    /**
     * Write a JavaFX image to a file
     * @param image the image to write, shall not be null
     * @param file the target file with the appropriate extension, shall not be null
     * @throws IOException if a writing problem occurs
     */
    public static void writeImage(Image image, File file) throws  IOException {
        notNull(image, "image argument shall not be null");
        notNull(file, "file argument shall not be null");

        BufferedImage buffer = toBuffer( image );
        String formatName = extractFormatName(file);
        ImageIO.write(buffer, formatName, file);
    }

    private static BufferedImage toBuffer(Image image ) {
        return SwingFXUtils.fromFXImage(image, null);
    }

    private static String extractFormatName(File file) {
        String filename = file.getName();
        return FilenameUtils.getExtension(filename).toLowerCase();
    }
}
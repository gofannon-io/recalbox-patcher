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

import io.gofannon.recalboxpatcher.patcher.patch.image.ImageDimension;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.apache.commons.lang3.Validate.*;


/**
 * Simple implementation of {@link ImageProcessor}
 */
public class ResizeAndConvertImageProcessor implements ImageProcessor {

    private ImageFormat imageFormat;
    private ImageDimension dimension;
    private File targetDirectory;
    private ImageProcessingResultImpl results;
    private SingleImageProcessingResultImpl currentImageResult;

    public void setDimension(ImageDimension dimension) {
        notNull(dimension, "dimension argument shall not be null");
        this.dimension = new ImageDimension(dimension);
    }

    public void setImageFormat(ImageFormat imageFormat) {
        this.imageFormat = imageFormat;
    }

    @Override
    public ImageProcessingResult processImageFiles(File inputDirectory, File outputDirectory) {
        notNull(inputDirectory, "inputDirectory shall not be null");
        notNull(outputDirectory, "outputDirectory shall not be null");

        this.targetDirectory = outputDirectory;
        this.results = new ImageProcessingResultImpl();

        for( File file : inputDirectory.listFiles()) {
            if( isImageFile(file) )
                processFile( file );
        }

        return results;
    }


    private boolean isImageFile(File file) {
        if( file.isFile()==false)
            return false;

        String extension = FilenameUtils.getExtension(file.getName());
        return ImageFormat.isSupportedExtension(extension);
    }


    private void processFile(File inputImageFile) {
        try {

            currentImageResult = new SingleImageProcessingResultImpl();
            currentImageResult.setInputFile(inputImageFile);

            BufferedImage inputImage = ImageIO.read(inputImageFile);
            currentImageResult.setRead(true);

            BufferedImage scaledImage = processFile(inputImage);

            File outputFile = computeOutputFile(inputImageFile);
            currentImageResult.setOutputFile(outputFile);
            currentImageResult.setTypeChanged( hasCurrentType(inputImageFile));

            boolean success = ImageIO.write(scaledImage, imageFormat.getImageIOFormatName(), outputFile);
            currentImageResult.setWritten(true);
            currentImageResult.setSuccess(success);

        } catch( IOException ioEx ) {
            currentImageResult.setSuccess(false);
            currentImageResult.setException(ioEx);
        }

        results.addResult(currentImageResult);
    }

    private boolean hasCurrentType(File file) {
        String extension = FilenameUtils.getExtension(file.getName());
        return imageFormat.isCompatibleExtension(extension);
    }

    private File computeOutputFile(File inputImageFile) {
        String baseName = FilenameUtils.getBaseName(inputImageFile.getName());
        String targetName = baseName+"."+ imageFormat.getDefaultExtension();
        return new File(targetDirectory, targetName);
    }

    private BufferedImage processFile(BufferedImage inputImage) {
        notNull(imageFormat, "imageFormat is not set");
        notNull(dimension, "dimension is not set");

        if( hasExpectedSize(inputImage)) {
            currentImageResult.setScaled(false);
            return inputImage;
        }


        java.awt.Image tempImage = inputImage.getScaledInstance(
                dimension.getWidth(),
                dimension.getHeight(),
                java.awt.Image.SCALE_SMOOTH);

        BufferedImage scaledImage = new BufferedImage(
                tempImage.getWidth(null),
                tempImage.getHeight(null),
                imageFormat.getType());

        Graphics2D g2 = scaledImage.createGraphics();
        try {

            g2.drawImage(tempImage, 0, 0, null);

        } finally {
            g2.dispose();
        }

        currentImageResult.setScaled(true);
        return scaledImage;
    }


    private boolean hasExpectedSize( BufferedImage image) {
        return image.getWidth(null) == dimension.getWidth()
                && image.getHeight(null) == dimension.getHeight();
    }

}
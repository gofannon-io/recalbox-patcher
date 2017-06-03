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

package io.gofannon.recalboxpatcher.patcher.view.processing;

import io.gofannon.recalboxpatcher.patcher.image.ImageProcessingResult;
import io.gofannon.recalboxpatcher.patcher.image.ResizeAndConvertImageProcessor;
import io.gofannon.recalboxpatcher.patcher.patch.common.ChangeDirectoryPathPatcher;
import io.gofannon.recalboxpatcher.patcher.patch.database.DefaultGameDatabasePatcher;
import io.gofannon.recalboxpatcher.patcher.patch.image.ImageDimension;
import io.gofannon.recalboxpatcher.patcher.processor.FileResourceProvider;
import io.gofannon.recalboxpatcher.patcher.processor.GameDatabasePatchProcessor;
import io.gofannon.recalboxpatcher.patcher.processor.PatchProcessingResult;
import javafx.concurrent.Task;

import java.io.File;

/**
 * Task for running the Recalbox patcher.
 */
public class RecalboxPatcherTask extends Task<PatchTaskResult> {

    private PatchTaskContext context;



    public RecalboxPatcherTask(PatchTaskContext context) {
        this.context = context;
    }

    @Override
    protected PatchTaskResult call() throws Exception {
        PatchProcessingResult gameProcessingResult = processingGames();
        ImageProcessingResult imageProcessingResult = processImages();


        PatchTaskResultImpl result = new PatchTaskResultImpl();
        result.setContext( context );
        result.setGameProcessingResult(gameProcessingResult);
        result.setImageProcessingResult(imageProcessingResult);

        return result;
    }

    private PatchProcessingResult processingGames() {
        File inputRecalboxFile = context.getInputRecalboxFile();
        File inputHyperspinFile = context.getInputHyperspinFile();
        File outputRecalboxFile = context.getOutputRecalboxFile();

        ChangeDirectoryPathPatcher imagePathPatcher = new ChangeDirectoryPathPatcher();
        imagePathPatcher.setDirectoryPath(context.getOutputRelativeImageDirectory());

        FileResourceProvider resourceProvider = new FileResourceProvider();
        resourceProvider.setInputRecalboxDatabaseFile(inputRecalboxFile);
        resourceProvider.setInputHyperspinDatabaseFile(inputHyperspinFile);
        resourceProvider.setOutputRecalboxDatabaseFile(outputRecalboxFile);
        resourceProvider.setImagePathPatcher(imagePathPatcher);

        DefaultGameDatabasePatcher patcher = new DefaultGameDatabasePatcher();
        patcher.setImagePathPatcher(imagePathPatcher);

        GameDatabasePatchProcessor processor = new GameDatabasePatchProcessor();
        processor.setResourceProvider(resourceProvider);
        processor.setGamePatcher(patcher);

        return processor.process();
    }

    private ImageProcessingResult processImages() {
        ResizeAndConvertImageProcessor imageProcessor = new ResizeAndConvertImageProcessor();
        ImageDimension imageDimension = new ImageDimension();
        imageDimension.setWidth(context.getWidthImage());
        imageDimension.setHeight(context.getHeightImage());
        imageProcessor.setDimension(imageDimension);
        imageProcessor.setImageFormat(context.getImageFormat());

        return imageProcessor.processImageFiles(context.getInputImageDirectory(), context.getOutputImageDirectory());
    }

}

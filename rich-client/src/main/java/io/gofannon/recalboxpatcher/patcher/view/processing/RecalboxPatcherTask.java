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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Task for running the Recalbox patcher.
 */
public class RecalboxPatcherTask extends Task<PatchTaskResult> {

    private PatchTaskContext context;



    public RecalboxPatcherTask(PatchTaskContext context) {
        this.context = context;
    }

    //FIXME enhance logging by separating logs and results

    @Override
    protected PatchTaskResult call() throws Exception {
        PatchProcessingResult processingResult = processingGames();
        ImageProcessingResult imageProcessingResult = processImages();


        PatchTaskResult result = new PatchTaskResult();
        result.setImageProcessingResult(imageProcessingResult);
        result.setLogs(createLogs(processingResult));


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
        imageProcessor.setType(context.getImageExtension());

        return imageProcessor.processImageFiles(context.getInputImageDirectory(), context.getOutputImageDirectory());
    }

    private List<String> createLogs(PatchProcessingResult result) {
        List<String> logs = new ArrayList<>();
        logs.add("Context: ");
        logs.add("Fichier d'entrée Recalbox: "+context.getInputRecalboxFile());
        logs.add("Fichier d'entrée Hyperspin: "+context.getInputHyperspinFile());
        logs.add("Fichier de sortie Recalbox: "+context.getOutputRecalboxFile());
        logs.add("Chemin des fichiers à télécharger: "+context.getInputImageDirectory());
        logs.add("Chemin des images dans le fichier: "+context.getOutputRelativeImageDirectory());
        logs.add("Hauteur des images: "+context.getHeightImage());
        logs.add("Largeur des images: "+context.getWidthImage());
        logs.add("Extension des images: "+context.getImageExtension());
        logs.add("Option 'Non trouvée' activée: "+context.isNotFoundOption());
        logs.add("Option 'Majuscule' activée: "+context.isUppercaseOption());
        logs.add("Option 'Nouveau Fichier' activée: "+context.isNewFileOption());
        logs.add("Option 'Ajout de nom' activée: "+context.isAddNameOption());

        logs.add("Resultat: ");
        logs.add("Nombre de jeux: "+result.getTotalGameCount());
        logs.add("Nombre de jeux patchés: "+result.getPatchedGameCount());
        logs.add("Nombre de jeux non patchés: "+result.getNotPatchedGameCount());

        logs.add("Liste des jeux patchés");
        List<String> patchedGames = result.getPatchedGames();
        Collections.sort(patchedGames);
        logs.addAll( addPrefixToList("\t", patchedGames));

        logs.add("Liste des jeux non patchés");
        List<String> notPatchedGames = result.getNotPatchedGames();
        Collections.sort(notPatchedGames);
        logs.addAll( addPrefixToList("\t", notPatchedGames));

        return logs;
    }

    private List<String> addPrefixToList(String prefix, List<String> list) {
        return list.stream()
                .map( s -> prefix+s)
                .collect(Collectors.toList());
    }
}

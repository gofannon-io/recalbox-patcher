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
import io.gofannon.recalboxpatcher.patcher.image.SingleImageProcessingResult;
import io.gofannon.recalboxpatcher.patcher.processor.PatchProcessingResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static io.gofannon.recalboxpatcher.patcher.view.utils.StringHelper.*;

class PatchTaskResultImpl implements PatchTaskResult {

    private PatchProcessingResult gameProcessingResult;
    private ImageProcessingResult imageProcessingResult;
    private PatchTaskContext context;

    @Override
    public boolean isSuccess() {
        boolean success = true;

        if (gameProcessingResult != null)
            success = gameProcessingResult.isSuccess();

        if (imageProcessingResult != null)
            success = success && imageProcessingResult.isSuccess();

        return success;
    }


    void setContext(PatchTaskContext context) {
        this.context = context;
    }


    @Override
    public PatchProcessingResult getGameProcessingResult() {
        return gameProcessingResult;
    }


    void setGameProcessingResult(PatchProcessingResult gameProcessingResult) {
        this.gameProcessingResult = gameProcessingResult;
    }


    @Override
    public List<String> getLogs() {
        return createLogs();
    }


    private List<String> createLogs() {
        List<String> logs = new ArrayList<>();
        appendContextLogs(logs);
        appendGameLogs(logs);
        appendImageLogs(logs);
        return logs;
    }


    private void appendContextLogs(List<String> logs) {
        if (context == null)
            return;

        logs.add("Contexte: ");
        logs.add("Fichier d'entrée Recalbox: " + context.getInputRecalboxFile());
        logs.add("Fichier d'entrée Hyperspin: " + context.getInputHyperspinFile());
        logs.add("Fichier de sortie Recalbox: " + context.getOutputRecalboxFile());
        logs.add("Chemin des fichiers à télécharger: " + context.getInputImageDirectory());
        logs.add("Chemin des images dans le fichier: " + context.getOutputRelativeImageDirectory());
        logs.add("Hauteur des images: " + context.getHeightImage());
        logs.add("Largeur des images: " + context.getWidthImage());
        logs.add("Format cible des images: " + context.getImageFormat().getDefaultExtension());
        logs.add("Option 'Non trouvée' activée: " + context.isNotFoundOption());
        logs.add("Option 'Majuscule' activée: " + context.isUppercaseOption());
        logs.add("Option 'Nouveau Fichier' activée: " + context.isNewFileOption());
        logs.add("Option 'Ajout de nom' activée: " + context.isAddNameOption());
    }


    private void appendGameLogs(List<String> logs) {
        if (gameProcessingResult == null)
            return;

        logs.add("Jeux: ");
        logs.add("Nombre de jeux: " + gameProcessingResult.getTotalGameCount());
        logs.add("Nombre de jeux patchés: " + gameProcessingResult.getPatchedGameCount());
        logs.add("Nombre de jeux non patchés: " + gameProcessingResult.getNotPatchedGameCount());

        logs.add("Liste des jeux patchés");
        List<String> patchedGames = gameProcessingResult.getPatchedGames();
        Collections.sort(patchedGames);
        logs.addAll(addPrefixToList(patchedGames));

        logs.add("Liste des jeux non patchés");
        List<String> notPatchedGames = gameProcessingResult.getNotPatchedGames();
        Collections.sort(notPatchedGames);
        logs.addAll(addPrefixToList(notPatchedGames));
    }


    private List<String> addPrefixToList(List<String> list) {
        return list.stream()
                .map(s -> "\t" + s)
                .collect(Collectors.toList());
    }


    private void appendImageLogs(List<String> logs) {
        if (imageProcessingResult == null)
            return;

        logs.add("Images ");
        logs.add("Total image count: " + imageProcessingResult.getImageCount());
        logs.add("Success image count: " + imageProcessingResult.getSucceededProcessedImageCount());
        logs.add("Failed image count: " + imageProcessingResult.getFailedProcessingImageCount());

        List<String> imageLogs = createImageLogs();
        logs.addAll(imageLogs);
    }


    private List<String> createImageLogs() {
        return imageProcessingResult.getProcessedImages().stream()
                .map(this::toLogLine)
                .collect(Collectors.toList());
    }

    private String toLogLine(SingleImageProcessingResult imageResult) {
        StringBuilder buffer = new StringBuilder("\t");

        if (imageResult.isSuccess()) {
            buffer.append("[SUCCES]");
        } else {
            buffer.append("[ECHEC]");
        }

        if (imageResult.getInputFile() != null) {
            buffer.append(" in=")
                    .append(imageResult.getInputFile().getName());
        }

        if (imageResult.getOutputFile() != null) {
            buffer.append(" out=")
                    .append(imageResult.getOutputFile().getName());
        }

        buffer.append(" opérations=");
        String operations = listOperations(imageResult);
        buffer.append(operations);

        if (imageResult.getException() != null) {
            buffer.append(" exception=");
            if (imageResult.getException().getMessage() != null) {
                buffer.append(imageResult.getException().getMessage())
                        .append("/");
            }
            buffer.append(imageResult.getException().getClass().getSimpleName());
        }

        return buffer.toString();
    }

    private String listOperations(SingleImageProcessingResult imageResult) {
        List<String> operations = new ArrayList<>();

        if (imageResult.isRead())
            operations.add("lecture");

        if (imageResult.isScaled())
            operations.add("redimensionnement");

        if (imageResult.isTypeChanged())
            operations.add("formatage");

        if (imageResult.isWritten())
            operations.add("écriture");

        if (operations.isEmpty()) {
            return "aucune";
        } else {
            return join(",", operations);
        }

    }

    @Override
    public ImageProcessingResult getImageProcessingResult() {
        return imageProcessingResult;
    }

    void setImageProcessingResult(ImageProcessingResult imageProcessingResult) {
        this.imageProcessingResult = imageProcessingResult;
    }
}

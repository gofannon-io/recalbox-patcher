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

package io.gofannon.recalboxpatcher.patcher.view;

import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Task for running the Recalbox patcher.
 */
public class RecalboxPatcherTask extends Task<Void> {

    private PatchTaskContext context;
    private List<String> logs;


    public RecalboxPatcherTask(PatchTaskContext context, List<String> logs) {
        this.context = context;
        this.logs = logs;
    }

    @Override
    protected Void call() throws Exception {
        Thread.sleep(60*000);
        createDummyLog();
        return null;
    }

    private void createDummyLog() {
        logs.add("Fichier d'entrée Recalbox: "+context.getInputRecalboxFile());
        logs.add("Fichier d'entrée Hyperspin: "+context.getInputHyperspinFile());
        logs.add("Fichier de sortie Recalbox: "+context.getOutputRecalboxFile());
        logs.add("Chemin des fichiers à télécharger: "+context.getInputImageDirectory());
        logs.add("Chemin des images dans le fichier: "+context.getOutputImageDirectory());
        logs.add("Hauteur des images: "+context.getHeightImage());
        logs.add("Largeur des images: "+context.getWidthImage());
        logs.add("Extension des images: "+context.getImageExtension());
        logs.add("Option 'Non trouvée' activée: "+context.isNotFoundOption());
        logs.add("Option 'Majuscule' activée: "+context.isUppercaseOption());
        logs.add("Option 'Nouveau Fichier' activée: "+context.isNewFileOption());
        logs.add("Option 'Ajout de nom' activée: "+context.isAddNameOption());
    }


    public List<String> getLogs() {
        return logs;
    }
}

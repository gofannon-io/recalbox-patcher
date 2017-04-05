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

package io.gofannon.recalboxpatcher.patcher;


import io.gofannon.recalboxpatcher.patcher.hyperspin.*;
import io.gofannon.recalboxpatcher.patcher.recalbox.*;

import static org.apache.commons.lang3.Validate.*;

/**
 * A processor tu patch a recalbox database
 */
public class PatchProcessor {

    private PatchResourceProvider resourceProvider;
    private PatchProcessingResult result;
    private HyperspinDatabase hyperspinDatabase;
    private RecalboxDatabase recalboxDatabase;
    private RecalboxDatabase patchedDatabase;
    private GamePatcher gamePatcher;


    public void setResourceProvider(PatchResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
    }

    public void setGamePatcher(GamePatcher gamePatcher) {
        this.gamePatcher = gamePatcher;
    }

    public PatchProcessingResult process() {
        initializeResult();
        checkResourceProvider();
        checkGamePatcher();

        startup();
        initializePatchedDatabase();
        patchRecalBoxDatabase();
        shutdown();


        return result;
    }

    private void initializeResult() {
        result = new PatchProcessingResult();
    }

    private void checkResourceProvider() {
        notNull(resourceProvider, "resourceProvider shall not be null");
    }

    private void checkGamePatcher() {
        notNull(gamePatcher,"gamePatcher shall not be null");
    }

    private void startup() {
        resourceProvider.startup();
    }

    private void initializePatchedDatabase() {
        patchedDatabase = resourceProvider.getPatchedRecalBoxDatabase();
    }

    private void patchRecalBoxDatabase() {
        recalboxDatabase.getAllGames().forEach(g -> patchGame(g));
    }

    private void patchGame(RecalboxGame recalboxGame) {
        HyperspinGame hyperspinGame = findGame(recalboxGame);
        if (hyperspinGame == null) {
            patchGameAlone(recalboxGame);
        } else {
            patchGameFromHyperspin(recalboxGame, hyperspinGame);
        }
    }

    private HyperspinGame findGame(RecalboxGame recalboxGame) {
        String name = recalboxGame.getNameFromPath();
        HyperspinGame hyperspinGame = hyperspinDatabase.findByName(name);
        if( hyperspinGame != null)
            return hyperspinGame;

        String nameNoDot = name.replace(": ", " - ");
        return hyperspinDatabase.findByName(nameNoDot);
    }

    private void patchGameAlone(RecalboxGame recalboxGame) {
        String name = recalboxGame.getNameFromPath();
        result.logGameNotFoundInHypersinDatabase(name);
        RecalboxGame patchedGame = resourceProvider.duplicateGame(recalboxGame);
        patchedDatabase.addGame( patchedGame );
    }

    private void patchGameFromHyperspin(RecalboxGame recalboxGame, HyperspinGame hyperspinGame) {
        String name = recalboxGame.getNameFromPath();
        RecalboxGame patchedGame = gamePatcher.patch(recalboxGame, hyperspinGame);
        result.logGamePatched(name);
        patchedDatabase.addGame( patchedGame );
    }

    private void shutdown() {
        resourceProvider.shutdown();
    }

    public PatchProcessingResult getPatchResult() {
        return result;
    }
}
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

package io.gofannon.recalboxpatcher.patcher.processor;


import io.gofannon.recalboxpatcher.patcher.patch.database.GameDatabasePatcher;
import io.gofannon.recalboxpatcher.patcher.model.hyperspin.*;
import io.gofannon.recalboxpatcher.patcher.model.recalbox.*;

import static org.apache.commons.lang3.Validate.*;

/**
 * A processor tu patch a recalbox database
 */
public class GameDatabasePatchProcessor {

    private GameDatabasePatchResourceProvider resourceProvider;
    private DefaultPatchProcessingResult result;
    private HyperspinDatabase hyperspinDatabase;
    private RecalboxDatabase recalboxDatabase;
    private RecalboxDatabase patchedDatabase;
    private GameDatabasePatcher gamePatcher;


    public void setResourceProvider(GameDatabasePatchResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
    }

    public void setGamePatcher(GameDatabasePatcher gamePatcher) {
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
        result = new DefaultPatchProcessingResult();
    }

    private void checkResourceProvider() {
        notNull(resourceProvider, "resourceProvider shall not be null");
    }

    private void checkGamePatcher() {
        notNull(gamePatcher,"gamePatcher shall not be null");
    }

    private void startup() {
        resourceProvider.startup();
        recalboxDatabase = resourceProvider.getInputRecalBoxDatabase();
        hyperspinDatabase = resourceProvider.getInputHyperspinDatabase();
    }

    private void initializePatchedDatabase() {
        patchedDatabase = resourceProvider.getOutputRecalBoxDatabase();
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
        String name = recalboxGame.getUniqueName();
        HyperspinGame hyperspinGame = hyperspinDatabase.findByName(name);
        if( hyperspinGame != null)
            return hyperspinGame;

        String nameNoDot = name.replace(": ", " - ");
        return hyperspinDatabase.findByName(nameNoDot);
    }

    private void patchGameAlone(RecalboxGame recalboxGame) {
        String name = recalboxGame.getUniqueName();
        RecalboxGame patchedGame = resourceProvider.duplicateGame(recalboxGame);
        patchedDatabase.addGame( patchedGame );
        result.logGameNotFoundInHypersinDatabase(name);
    }

    private void patchGameFromHyperspin(RecalboxGame recalboxGame, HyperspinGame hyperspinGame) {
        String name = recalboxGame.getUniqueName();
        RecalboxGame patchedGame = gamePatcher.patch(recalboxGame, hyperspinGame);
        patchedDatabase.addGame( patchedGame );
        result.logGamePatched(name);
    }

    private void shutdown() {
        resourceProvider.shutdown();
    }

    public PatchProcessingResult getPatchResult() {
        return result;
    }
}
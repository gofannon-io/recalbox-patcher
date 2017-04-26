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

import io.gofannon.recalboxpatcher.patcher.patch.common.PathPatcher;
import io.gofannon.recalboxpatcher.patcher.model.hyperspin.HyperspinDatabase;
import io.gofannon.recalboxpatcher.patcher.model.recalbox.RecalboxDatabase;
import io.gofannon.recalboxpatcher.patcher.model.recalbox.RecalboxGame;

/**
 * A provider of resources to patch a RecalBox database
 */
public interface GameDatabasePatchResourceProvider {

    /**
     * Method called before the processing
     */
    void startup();

    /**
     * Method called after the processing
     */
    void shutdown();

    /**
     * Get the Hyperspin database
     *
     * @return a non <code>null</code> database
     */
    HyperspinDatabase getInputHyperspinDatabase();


    /**
     * Get the Recalbox database
     * <p>This is the database that contains some errors</p>
     *
     * @return a non <code>null</code> database
     */
    RecalboxDatabase getInputRecalBoxDatabase();

    /**
     * Get the patcher of the image path
     * @return a non null path patcher
     */
    PathPatcher getImagePathPatcher();

    /**
     * Get the RecalBox database.
     * <p>At the beginning, this database is empty.</p>
     *
     * @return a non <code>null</code> database
     */
    RecalboxDatabase getOutputRecalBoxDatabase();

    /**
     * Duplicate a game
     *
     * @param recalBoxGame a game, may be <code>null</code>
     * @return <code>null</code> if the recalBoxGame is <code>null</code> or a clone of the recalBoxGame
     */
    RecalboxGame duplicateGame(RecalboxGame recalBoxGame);

}

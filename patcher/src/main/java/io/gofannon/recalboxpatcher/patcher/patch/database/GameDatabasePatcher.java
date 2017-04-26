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
package io.gofannon.recalboxpatcher.patcher.patch.database;

import io.gofannon.recalboxpatcher.patcher.model.hyperspin.HyperspinGame;
import io.gofannon.recalboxpatcher.patcher.model.recalbox.RecalboxGame;

/**
 * Patcher of Recalbox games.
 * <p>
 * The patcher is based on the extraction of information from an Hyperspin game to complete the Recalbox game.
 * </p>
 */
public interface GameDatabasePatcher {

    /**
     * Patch a Recalbox game
     *
     * @param recalboxGame  the original Recalbox game to patch
     * @param hyperspinGame the Hyperspin game containing
     * @return a new instance of Recalbox
     * @throws NullPointerException if an argument is null
     */
    RecalboxGame patch(RecalboxGame recalboxGame, HyperspinGame hyperspinGame) throws NullPointerException;

}

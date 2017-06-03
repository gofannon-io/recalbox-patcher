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

import io.gofannon.recalboxpatcher.patcher.patch.common.PathPatcher;
import io.gofannon.recalboxpatcher.patcher.model.hyperspin.HyperspinGame;
import io.gofannon.recalboxpatcher.patcher.model.recalbox.RecalboxGame;

import static org.apache.commons.lang3.Validate.*;

/**
 * Implementation of a {@link GameDatabasePatcher}
 */
public class DefaultGameDatabasePatcher implements GameDatabasePatcher {

    private PathPatcher imagePathPatcher;


    /**
     * Set image path patcher
     * @param imagePathPatcher the patcher of image path, cannot be null
     */
    public void setImagePathPatcher(PathPatcher imagePathPatcher) {
        notNull(imagePathPatcher, "imagePathPatcher argument shall not be null");
        this.imagePathPatcher = imagePathPatcher;
    }

    @Override
    public RecalboxGame patch(RecalboxGame recalboxGame, HyperspinGame hyperspinGame) {
        checkInitialized();

        RecalboxGame patchedGame = new RecalboxGame();

        patchedGame.setPath(recalboxGame.getPath());
        patchedGame.setRating(recalboxGame.getRating());
        patchedGame.setName(hyperspinGame.getDescription());
        patchedGame.setDesc(hyperspinGame.getSynopsis());
        patchedGame.setReleaseDate(hyperspinGame.getYear() + "0124T000000");
        patchedGame.setDeveloper(hyperspinGame.getDeveloper());
        patchedGame.setPublisher(hyperspinGame.getManufacturer());
        patchedGame.setGenre(hyperspinGame.getGenre());
        patchedGame.setPlayCount(hyperspinGame.getPlayerCount());
        patchedGame.setLastPlayed("");
        patchedGame.setImage(patchedImagePath(recalboxGame));

        return patchedGame;
    }

    private void checkInitialized() {
        notNull(imagePathPatcher, "imagePathPatcher argument not set");
    }

    private String patchedImagePath(RecalboxGame game) {
        String originalPath = game.getImage();
        return imagePathPatcher.patch(originalPath);
    }
}
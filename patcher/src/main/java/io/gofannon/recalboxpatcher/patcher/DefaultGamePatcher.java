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


import io.gofannon.recalboxpatcher.patcher.hyperspin.HyperspinGame;
import io.gofannon.recalboxpatcher.patcher.recalbox.RecalboxGame;

public class DefaultGamePatcher implements GamePatcher {

    private GamePatchContext context;

    @Override
    public void setContext(GamePatchContext context) {
        this.context = context;
    }

    @Override
    public RecalboxGame patch(RecalboxGame recalboxGame, HyperspinGame hyperspinGame) {
        RecalboxGame patchedGame = new RecalboxGame();

        patchedGame.setPath(recalboxGame.getPath());
        patchedGame.setRating(recalboxGame.getRating());
        patchedGame.setName(hyperspinGame.getDescription());
        patchedGame.setDesc(hyperspinGame.getSynopsis());
        patchedGame.setReleaseDate(hyperspinGame.getYear()+"0124T000000");
        patchedGame.setDeveloper(hyperspinGame.getDeveloper());
        patchedGame.setPublisher(hyperspinGame.getManufacturer());
        patchedGame.setGenre(hyperspinGame.getGenre());
        patchedGame.setPlayCount(hyperspinGame.getPlayers());
        patchedGame.setLastPlayed("");

        String patchedImage = patchImagePath(recalboxGame.getImage());
        patchedGame.setImage(patchedImage);

        return patchedGame;
    }

    private String patchImagePath(String imagePath) {
        if( imagePath == null)
            return null;
        int lastSeparator = imagePath.lastIndexOf("/");
        String imageName = imagePath.substring(lastSeparator+1);
        String patchedImagePath = context.getImagePath()+imageName;
        return patchedImagePath.replace("//", "/");
    }
}

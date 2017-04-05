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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class DefaultGamePatcherTest {

    DefaultGamePatcher gamePatcher;
    GamePatchContext context;
    RecalboxGame recalboxGame;
    HyperspinGame hyperspinGame;


    @Before
    public void setUp() throws Exception {
        context = new GamePatchContext();
        context.setImagePath("/test/image");

        gamePatcher = new DefaultGamePatcher();

        recalboxGame = new RecalboxGame();
        recalboxGame.setImage("/recalbox/image.png");
        recalboxGame.setPath("/recalbox/game");
        recalboxGame.setName("recalboxName");
        recalboxGame.setReleaseDate("2001");
        recalboxGame.setDeveloper("recalboxDeveloper");
        recalboxGame.setPublisher("recalboxPublisher");
        recalboxGame.setGenre("recalboxGenre");
        recalboxGame.setPlayCount(300);
        recalboxGame.setLastPlayed( "recalboxLastPlayed" );
        recalboxGame.setRating("recalboxRating");


        hyperspinGame = new HyperspinGame();
        hyperspinGame.setDescription("hyperspinDescription");
        hyperspinGame.setSynopsis("hyperspinSynopsis");
        hyperspinGame.setYear(2000);
        hyperspinGame.setDeveloper("hyperspinDeveloper");
        hyperspinGame.setManufacturer("hyperspinManufacturer");
        hyperspinGame.setGenre("hyperspinGenre");
        hyperspinGame.setPlayers(50);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void setContext() throws Exception {
    }

    @Test
    public void patch_shallHandleImagePathWithSeparator() throws Exception {
        context.setImagePath("/test/image/");
        gamePatcher.setContext(context);

        RecalboxGame patchedGame = gamePatcher.patch(recalboxGame, hyperspinGame);
        assertThat(patchedGame.getImage())
                .isEqualTo("/test/image/image.png");
    }

    @Test
    public void patch_shallHandleImagePathWithTwoSeparator() throws Exception {
        context.setImagePath("/test/image//");
        gamePatcher.setContext(context);

        RecalboxGame patchedGame = gamePatcher.patch(recalboxGame, hyperspinGame);
        assertThat(patchedGame.getImage())
                .isEqualTo("/test/image/image.png");
    }

    @Test
    public void patch_shallHandleImagePathWithoutSeparator() throws Exception {
        context.setImagePath("/test/image");
        gamePatcher.setContext(context);

        RecalboxGame patchedGame = gamePatcher.patch(recalboxGame, hyperspinGame);
        assertThat(patchedGame.getImage())
                .isEqualTo("/test/image/image.png");
    }

    @Test(expected = NullPointerException.class)
    public void patch_shallFailWithoutContext() throws Exception {
        gamePatcher.patch(recalboxGame,hyperspinGame);
    }

    @Test(expected = NullPointerException.class)
    public void patch_shallFailIfRecalboxGameIsNull() throws Exception {
        gamePatcher.setContext(context);
        gamePatcher.patch(null,hyperspinGame);
    }

    @Test(expected = NullPointerException.class)
    public void patch_shallFailIfHypersinGameIsNull() throws Exception {
        gamePatcher.setContext(context);
        gamePatcher.patch(recalboxGame,null);
    }

    @Test
    public void patch() throws Exception {
        gamePatcher.setContext(context);
        gamePatcher.patch(recalboxGame,hyperspinGame);

        RecalboxGame patchedGame = gamePatcher.patch(recalboxGame,hyperspinGame);
        assertThat(patchedGame)
                .extracting(
                        "path",
                        "name",
                        "desc",
                        "image",
                        "rating",
                        "releaseDate",
                        "developer",
                        "publisher",
                        "genre",
                        "playCount",
                        "lastPlayed")
                .containsExactly(
                        "/recalbox/game",
                        "hyperspinDescription",
                        "hyperspinSynopsis",
                        "/test/image/image.png",
                        "recalboxRating",
                        "20000124T000000",
                        "hyperspinDeveloper",
                        "hyperspinManufacturer",
                        "hyperspinGenre",
                        50,
                        ""
                );
    }

}
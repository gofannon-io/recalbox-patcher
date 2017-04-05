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

import io.gofannon.recalboxpatcher.patcher.hyperspin.HyperspinDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import io.gofannon.recalboxpatcher.patcher.hyperspin.HyperspinDatabase;
import io.gofannon.recalboxpatcher.patcher.recalbox.RecalboxDatabase;
import io.gofannon.recalboxpatcher.patcher.recalbox.RecalboxGame;

import static io.gofannon.recalboxpatcher.patcher.TestResourceHelper.*;

import java.io.File;

import static org.assertj.core.api.Assertions.*;

public class FileResourceProviderTest {

    static final String TARGET_IMAGE_PATH = "/new/path";

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    FileResourceProvider provider;

    File hyperspinFile;
    File recalboxSourceFile;
    File recalboxTargetFile;

    @Before
    public void setUp() throws Exception {
        hyperspinFile = createHyperspinFile(tempFolder);
        recalboxSourceFile = createRecalboxFile(tempFolder);
        recalboxTargetFile = new File(tempFolder.getRoot(), "recalbox-fixed.xml");

        provider = new FileResourceProvider();
        provider.setTargetImagePath(TARGET_IMAGE_PATH);
        provider.setHyperspinDatabaseFile(hyperspinFile);
        provider.setRecalboxDatabaseSourceFile(recalboxSourceFile);
        provider.setRecalboxDatabaseTargetFile(recalboxTargetFile);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getHyperspinDatabase() throws Exception {
        HyperspinDatabase database = provider.getHyperspinDatabase();
        assertThat(database)
            .isNotNull();
        assertThat(database.getGameNames())
                .isNotEmpty();
    }

    @Test(expected = NullPointerException.class)
    public void getHyperspinDatabase_shallNotHandleNull() throws Exception {
        provider.setHyperspinDatabaseFile(null);
        provider.getHyperspinDatabase();
    }

    @Test
    public void getRecalBoxDatabase() throws Exception {
        RecalboxDatabase database = provider.getRecalBoxDatabase();
        assertThat(database)
                .isNotNull();
        assertThat(database.getAllGames())
                .isNotEmpty();
    }

    @Test(expected = NullPointerException.class)
    public void getRecalBoxDatabase_shallNotHandleNull() throws Exception {
        provider.setRecalboxDatabaseSourceFile(null);
        provider.getRecalBoxDatabase();
    }

    @Test(expected = NullPointerException.class)
    public void startup_shallFailWhenNoRecalboxSourceFile() {
        provider.setRecalboxDatabaseSourceFile(null);
        provider.setRecalboxDatabaseTargetFile(recalboxTargetFile);
        provider.setHyperspinDatabaseFile(hyperspinFile);
        provider.setTargetImagePath("/images/");
        provider.startup();
    }

    @Test(expected = NullPointerException.class)
    public void startup_shallFailWhenNoRecalboxTargetFile() {
        provider.setRecalboxDatabaseSourceFile(recalboxSourceFile);
        provider.setRecalboxDatabaseTargetFile(null);
        provider.setHyperspinDatabaseFile(hyperspinFile);
        provider.setTargetImagePath("/images/");
        provider.startup();
    }

    @Test(expected = NullPointerException.class)
    public void startup_shallFailWhenNoHyperspintFile() {
        provider.setRecalboxDatabaseSourceFile(recalboxSourceFile);
        provider.setRecalboxDatabaseTargetFile(recalboxTargetFile);
        provider.setHyperspinDatabaseFile(null);
        provider.setTargetImagePath("/images/");
        provider.startup();
    }

    @Test(expected = NullPointerException.class)
    public void startup_shallFailWhenTargetImagePathIsNotSet() {
        provider.setRecalboxDatabaseSourceFile(recalboxSourceFile);
        provider.setRecalboxDatabaseTargetFile(recalboxTargetFile);
        provider.setHyperspinDatabaseFile(null);
        provider.setTargetImagePath(null);
        provider.startup();
    }

    @Test
    public void startup_nominal() {
        provider.startup();
    }


    @Test(expected = IllegalStateException.class)
    public void shutdown_shallBeCallAfterStartup() {
        provider.shutdown();
    }

    @Test
    public void shutdown_nominal() {
        provider.startup();

        assertThat(recalboxTargetFile).doesNotExist();

        provider.shutdown();

        assertThat(recalboxTargetFile)
                .exists();
        assertThat(recalboxTargetFile.length())
                .isGreaterThan(0l);
    }


    @Test
    public void getPatchedRecalBoxDatabase_nominal() {
        RecalboxDatabase database = provider.getPatchedRecalBoxDatabase();
        assertThat(database)
                .isNotNull();
        assertThat(database.getAllGames())
                .isEmpty();
    }

    @Test
    public void duplicateGame_shallReturnNullWhenArgumentIsNull() {
        assertThat(
                provider.duplicateGame(null)
            ).isNull();
    }

    @Test
    public void duplicateGame_nominal() {
        RecalboxGame original = new RecalboxGame();
        original.setPath("/path/");
        original.setRating("rating**");
        original.setPlayCount(13);
        original.setGenre("genre");
        original.setPublisher("publisher");
        original.setDeveloper("developer");
        original.setReleaseDate("03/05/04");
        original.setName("The Game");
        original.setImage("/image.png");
        original.setDesc("bla bla bla");
        original.setLastPlayed("08/03/2012");


        RecalboxGame clone = provider.duplicateGame(original);
        assertThat(clone)
                .extracting("path", "rating", "playCount",
                        "genre", "publisher", "developer",
                        "releaseDate", "name", "image",
                        "desc", "lastPlayed")
                .containsExactly("/path/", "rating**", 13,
                        "genre", "publisher", "developer",
                        "03/05/04", "The Game", "/image.png",
                        "bla bla bla", "08/03/2012");
    }
}
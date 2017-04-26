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

import io.gofannon.recalboxpatcher.patcher.patch.common.ChangeDirectoryPathPatcher;
import io.gofannon.recalboxpatcher.patcher.processor.FileResourceProvider;
import io.gofannon.recalboxpatcher.patcher.model.hyperspin.HyperspinDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import io.gofannon.recalboxpatcher.patcher.model.recalbox.RecalboxDatabase;
import io.gofannon.recalboxpatcher.patcher.model.recalbox.RecalboxGame;

import static io.gofannon.recalboxpatcher.patcher.TestResourceHelper.*;

import java.io.File;

import static org.assertj.core.api.Assertions.*;

public class FileResourceProviderTest {

    static final String TARGET_IMAGE_PATH = "/new/path";

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    ChangeDirectoryPathPatcher pathPatcher;
    FileResourceProvider provider;

    File hyperspinFile;
    File recalboxSourceFile;
    File recalboxTargetFile;

    @Before
    public void setUp() throws Exception {
        pathPatcher = new ChangeDirectoryPathPatcher(TARGET_IMAGE_PATH);

        hyperspinFile = createHyperspinFile(tempFolder);
        recalboxSourceFile = createRecalboxFile(tempFolder);
        recalboxTargetFile = new File(tempFolder.getRoot(), "recalbox-fixed.xml");

        provider = new FileResourceProvider();
        provider.setImagePathPatcher(pathPatcher);
        provider.setInputHyperspinDatabaseFile(hyperspinFile);
        provider.setInputRecalboxDatabaseFile(recalboxSourceFile);
        provider.setOutputRecalboxDatabaseFile(recalboxTargetFile);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getHyperspinDatabase() throws Exception {
        provider.startup();

        HyperspinDatabase database = provider.getInputHyperspinDatabase();
        assertThat(database)
            .isNotNull();
        assertThat(database.getGameNames())
                .isNotEmpty();
    }

    @Test
    public void getRecalBoxDatabase() throws Exception {
        provider.startup();

        RecalboxDatabase database = provider.getInputRecalBoxDatabase();
        assertThat(database)
                .isNotNull();
        assertThat(database.getAllGames())
                .isNotEmpty();
    }

    @Test(expected = NullPointerException.class)
    public void startup_shallFailWhenNoRecalboxSourceFile() {
        provider.setInputRecalboxDatabaseFile(null);
        provider.setOutputRecalboxDatabaseFile(recalboxTargetFile);
        provider.setInputHyperspinDatabaseFile(hyperspinFile);
        provider.setImagePathPatcher(pathPatcher);
        provider.startup();
    }

    @Test(expected = NullPointerException.class)
    public void startup_shallFailWhenNoRecalboxTargetFile() {
        provider.setInputRecalboxDatabaseFile(recalboxSourceFile);
        provider.setOutputRecalboxDatabaseFile(null);
        provider.setInputHyperspinDatabaseFile(hyperspinFile);
        provider.setImagePathPatcher(pathPatcher);
        provider.startup();
    }

    @Test(expected = NullPointerException.class)
    public void startup_shallFailWhenNoHyperspintFile() {
        provider.setInputRecalboxDatabaseFile(recalboxSourceFile);
        provider.setOutputRecalboxDatabaseFile(recalboxTargetFile);
        provider.setInputHyperspinDatabaseFile(null);
        provider.setImagePathPatcher(pathPatcher);
        provider.startup();
    }

    @Test(expected = NullPointerException.class)
    public void startup_shallFailWhenImagePathPatcherIsNotSet() {
        provider.setInputRecalboxDatabaseFile(recalboxSourceFile);
        provider.setOutputRecalboxDatabaseFile(recalboxTargetFile);
        provider.setInputHyperspinDatabaseFile(null);
        provider.setImagePathPatcher(null);
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
        RecalboxDatabase database = provider.getOutputRecalBoxDatabase();
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
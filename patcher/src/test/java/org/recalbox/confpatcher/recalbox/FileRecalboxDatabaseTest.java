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

package org.recalbox.confpatcher.recalbox;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.recalbox.confpatcher.IORuntimeException;
import org.recalbox.confpatcher.TestResourceHelper;
import static org.assertj.core.api.Assertions.*;

import java.io.*;
import java.util.List;


public class FileRecalboxDatabaseTest {

    FileRecalboxDatabase database;

    @Rule
    public TemporaryFolder rootFolder = new TemporaryFolder();


    @Before
    public void setUp() throws Exception {
        database = new FileRecalboxDatabase();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test(expected = NullPointerException.class)
    public void loadFromFile_shallNotLoadFromNullFile() throws Exception {
        database.loadFromFile(null);
    }

    @Test(expected = IORuntimeException.class)
    public void loadFromFile_shallNotLoadFromNotExistingFile() throws Exception {
        File notExistingFile = new File("notexist.missing");
        database.loadFromFile(notExistingFile);
    }

    @Test
    public void loadFromFile_nominal() throws Exception {
        File file = TestResourceHelper.createRecalboxFile(rootFolder);

        assertThat( database.getAllGames() )
                .isEmpty();

        database.loadFromFile(file);

        assertThat( database.getAllGames() )
                .isNotEmpty();
    }

    @Test(expected = NullPointerException.class)
    public void loadFromStream_shallNotAcceptNullArg() throws Exception {
        database.loadFromStream(null);
    }

    @Test
    public void loadFromStream_nominal() throws Exception {

        assertThat( database.getAllGames() )
                .isEmpty();

        try(InputStream in = TestResourceHelper.openRecalboxStream() ) {
            database.loadFromStream(in);
        }

        assertThat( database.getAllGames() )
                .isNotEmpty();
    }

    @Test(expected = NullPointerException.class)
    public void saveToFile_shallFailWhenFileIsNull() throws Exception {
        database.saveToFile(null);
    }


    @Test
    public void saveToFile() throws Exception {
        try(InputStream in = TestResourceHelper.openRecalboxStream() ) {
            database.loadFromStream(in);
        }

        File file = new File( rootFolder.getRoot(), "file1.xml" );
        assertThat(file).doesNotExist();

        database.saveToFile(file);

        assertThat(file).exists();
    }

    @Test(expected = NullPointerException.class)
    public void saveToStream_shallNotAcceptNullFile() throws Exception {
        database.saveToStream(null);
    }

    @Test
    public void saveToStream_null() throws Exception {
        try(InputStream in = TestResourceHelper.openRecalboxStream() ) {
            database.loadFromStream(in);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        assertThat(out.toByteArray()).isEmpty();
        database.saveToStream(out);

        assertThat(out.toByteArray()).isNotEmpty();
    }

    @Test
    public void getGameNamesFromPath_shallReturnEmptyListByDefault() throws Exception {
        List<String> names = database.getGameNamesFromPath();
        assertThat(names)
                .isEmpty();
    }

    @Test
    public void getGameNamesFromPath_shallReturnFilledListWhenFilled() throws Exception {
        try(InputStream in = TestResourceHelper.openRecalboxStream() ) {
            database.loadFromStream(in);
        }

        List<String> names = database.getGameNamesFromPath();
        assertThat(names)
                .isNotEmpty();
    }

    @Test
    public void getAllGames_shallReturnEmptyListWhenNoGamesInDatabase() throws Exception {
        assertThat( database.getAllGames() ).isEmpty();
    }

    @Test
    public void getAllGames_shallReturnFilledListWhenGamesInDatabase() throws Exception {
        try(InputStream in = TestResourceHelper.openRecalboxStream() ) {
            database.loadFromStream(in);
        }

        assertThat( database.getAllGames() ).isNotEmpty();
    }
}
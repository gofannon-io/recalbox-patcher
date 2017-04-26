/*
 * Copyright 2016 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gofannon.recalboxpatcher.patcher.model.hyperspin;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static io.gofannon.recalboxpatcher.patcher.TestResourceHelper.*;

public class FileHyperspinDatabaseTest {

    File hyperspinFile;
    FileHyperspinDatabase database;
    HyperspinGame game;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        hyperspinFile = new File(folder.getRoot(), "hyperspin.xml");
        createHyperspinFile(hyperspinFile);

        database = new FileHyperspinDatabase();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testLoadFromFile() throws IOException {
        database.loadFromFile(hyperspinFile);
    }

    @Test
    public void testFindByName_notFound() throws IOException {
        database.loadFromFile(hyperspinFile);
        game = database.findByName("not a game");
        assertThat(game).isNull();
    }

    @Test
    public void testFindByName() throws IOException {
        database.loadFromFile(hyperspinFile);
        game = database.findByName("Life Force - Salamander (Europe)");
        //@formatter:off
        assertThat( game )
            .extracting(HyperspinGame::getName, HyperspinGame::getDescription)
            .containsExactly(
                "Life Force - Salamander (Europe)",
                "Life Force - Salamander"
            );
        //@formatter:on
    }

    @Test
    public void testPrintGameNames() throws IOException {
        database.loadFromFile(hyperspinFile);
        System.out.println("Hyperspin game list:");
        database.getGameNames().stream().forEach(n -> System.out.println(n));
        System.out.println("---------");
    }
    
    @Test
    public void testPrintEscapedGameNames() throws IOException {
        database.loadFromFile(hyperspinFile);
        System.out.println("Hyperspin game list (escaped):");
        database.getGameNames().stream()
            .map(input -> StringEscapeUtils.unescapeXml(input))
            .forEach(n -> System.out.println(n) );
        System.out.println("---------");
    }

    @Test
    public void testLookupEuropeName() throws IOException {
        database.loadFromFile(hyperspinFile);
        game = database.findByName("Astyanax (Europe)");
        assertThat(game).isNotNull();
    }
}
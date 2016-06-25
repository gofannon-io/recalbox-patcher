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
package org.recalbox.confpatcher.recalbox;

import static org.junit.Assert.fail;

import java.io.File;
import java.net.URI;
import java.net.URL;

import org.junit.*;

public class RecalboxDatabaseTest {

    File recalboxFile;
    RecalboxDatabase database;
    RecalboxGame game;

    @Before
    public void setUp() throws Exception {
        URL url = getClass().getResource("/Recalbox gamelist.xml");
        URI uri = url.toURI();
        recalboxFile = new File(uri);

        database = new RecalboxDatabase();

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    @Ignore
    public void testRecalboxDatabase() {
        fail("Not yet implemented");
    }

    @Test
    @Ignore
    public void testLoadFromFile() {
        fail("Not yet implemented");
    }

    @Test
    @Ignore
    public void testFix() {
        fail("Not yet implemented");
    }

    @Test
    @Ignore
    public void testSaveToFile() {
        fail("Not yet implemented");
    }

    @Test
    public void testPrintList() throws Exception {
        database.loadFromFile(recalboxFile);
        System.out.println("List of games :");
        database.getGameNamesFromPath().stream().forEach(name -> System.out.println(name));
    }

}

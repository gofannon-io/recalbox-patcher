/*
 * Copyright 2016-2017 the original author or authors.
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
package org.recalbox.confpatcher;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class ConfigurationPatcherTest {

    @Rule
    public TemporaryFolder rootFolder = new TemporaryFolder();

    File hyperspinFile;
    File recalboxFile;
    File recalboxBackupFile;

    ConfigurationPatcher patcher;

    @Before
    public void setUp() throws Exception {
        patcher = new ConfigurationPatcher();

        hyperspinFile = createResourceFile(rootFolder.getRoot(), "Hyperspin Nintendo Entertainment System.xml");
        recalboxFile = createResourceFile(rootFolder.getRoot(), "Recalbox gamelist.xml");
        recalboxBackupFile = new File(rootFolder.getRoot(), "Recalbox gamelist.xml.000.backup");
    }

    private File createResourceFile(File parent, String resourceName) throws IOException {
        File file = new File(parent, resourceName);
        URL resourceURL = getClass().getResource("/" + resourceName);
        FileUtils.copyURLToFile(resourceURL, file);
        return file;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testFull() throws Exception {
        patcher.setHyperspinDatabaseFile(hyperspinFile);
        patcher.setRecalBoxDatabaseFile(recalboxFile);

        patcher.process(); // TODO add report generation

        assertThat(recalboxBackupFile).exists().isFile();
    }

    @Test
    public void testReal() throws Exception {
        File targetDir = new File(SystemUtils.getJavaIoTmpDir(), "recalbox");

        hyperspinFile = createResourceFile(targetDir, "Hyperspin Nintendo Entertainment System.xml");
        recalboxFile = createResourceFile(targetDir, "Recalbox gamelist.xml");

        patcher.setHyperspinDatabaseFile(hyperspinFile);
        patcher.setRecalBoxDatabaseFile(recalboxFile);

        patcher.process(); // TODO add report generation
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

}
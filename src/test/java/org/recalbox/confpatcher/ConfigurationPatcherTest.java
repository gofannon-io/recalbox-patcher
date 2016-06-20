package org.recalbox.confpatcher;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

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
        hyperspinFile = createResourceFile(SystemUtils.getJavaIoTmpDir(), "Hyperspin Nintendo Entertainment System.xml");
        recalboxFile = createResourceFile(SystemUtils.getJavaIoTmpDir(), "Recalbox gamelist.xml");

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
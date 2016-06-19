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

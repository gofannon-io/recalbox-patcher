package org.recalbox.confpatcher.hyperspin;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.recalbox.confpatcher.hyperspin.HyperspinDatabase;
import org.recalbox.confpatcher.hyperspin.HyperspinGame;

public class HyperspinDatabaseTest {

    File hyperspinFile;
    HyperspinDatabase database;
    HyperspinGame game;

    @Before
    public void setUp() throws Exception {
        URL url = getClass().getResource("/Hyperspin Nintendo Entertainment System.xml");
        URI uri = url.toURI();
        hyperspinFile = new File(uri);

        database = new HyperspinDatabase();
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
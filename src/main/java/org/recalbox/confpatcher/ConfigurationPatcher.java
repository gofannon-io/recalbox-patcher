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
package org.recalbox.confpatcher;

import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.recalbox.confpatcher.hyperspin.HyperspinDatabase;
import org.recalbox.confpatcher.hyperspin.HyperspinGame;
import org.recalbox.confpatcher.recalbox.RecalboxDatabase;
import org.recalbox.confpatcher.recalbox.RecalboxGame;

public class ConfigurationPatcher {
    
    private File hyperspinDatabaseFile;
    private File recalBoxDatabaseFile;
    private HyperspinDatabase hyperspinDatabase;
    private RecalboxDatabase recalboxDatabase;
    
    private FixResult operationLogger = new FixResult();
    
    public void setHyperspinDatabaseFile( File hyperspinDatabaseFile ) {
        this.hyperspinDatabaseFile = hyperspinDatabaseFile;
    }
    
    public void setRecalBoxDatabaseFile(File recalBoxDatabaseFile) {
        this.recalBoxDatabaseFile = recalBoxDatabaseFile;
    }
    
    public void process() throws IOException, JAXBException {
        backupRecalBoxDatabaseFile();
        createHyperspinDatabase();
        loadRecalBoxDatabase();
        fixRecalBoxDatabase();
        saveRecalBoxDatabase();
        printStats();
    }
    
    private void backupRecalBoxDatabaseFile() throws IOException {
        File backupFile = backupFile(recalBoxDatabaseFile);
        FileUtils.copyFile(recalBoxDatabaseFile, backupFile);
    }

    private static File backupFile(File file) {
        for( int counter = 0; counter < 999; counter++) {
            String filename = String.format("%s.%03d.backup", file.getName(), counter);
            File bckFile = new File( file.getParentFile(), filename);
            if( bckFile.exists()==false)
                return bckFile;
        }
        
        throw new TooManyBackupFileException("Cannot backup file "+file.getAbsolutePath()+", too many backup file");
    }
    
    private void createHyperspinDatabase() throws IOException {
        hyperspinDatabase = new HyperspinDatabase();
        hyperspinDatabase.loadFromFile(hyperspinDatabaseFile);
    }

    private void loadRecalBoxDatabase() throws IOException {
        recalboxDatabase = new RecalboxDatabase();
        recalboxDatabase.loadFromFile(recalBoxDatabaseFile);
    }

    private void fixRecalBoxDatabase() {
        recalboxDatabase.getAllGames().forEach(g -> fixGame(g));
    }
    
    private void fixGame(RecalboxGame recalboxGame) {
        String name = recalboxGame.getNameFromPath();
        
        HyperspinGame hyperspinGame = findGame(recalboxGame);
        if (hyperspinGame == null) {
            operationLogger.logGameNotFoundInHypersinDatabase(name);
            return;
        }
        
        recalboxGame.setName(hyperspinGame.getDescription());
        recalboxGame.setDesc(hyperspinGame.getSynopsis());
        recalboxGame.setReleasedate(hyperspinGame.getYear()+"0124T000000");
        recalboxGame.setDeveloper(hyperspinGame.getManufacturer());
        recalboxGame.setPublisher(hyperspinGame.getDeveloper());
        recalboxGame.setGenre(hyperspinGame.getGenre());
        recalboxGame.setPlayers(hyperspinGame.getPlayers());
        
        operationLogger.logGameFixed(name);
    }
    
    private HyperspinGame findGame(RecalboxGame recalboxGame) {
        String name = recalboxGame.getNameFromPath();
        HyperspinGame hyperspinGame = hyperspinDatabase.findByName(name);
        if( hyperspinGame != null)
            return hyperspinGame;
        
        
        String nameNoDot = name.replace(": ", " - ");
        hyperspinGame = hyperspinDatabase.findByName(nameNoDot);
        if( hyperspinGame != null)
            return hyperspinGame;

        return null;
    }

    private void saveRecalBoxDatabase() throws IOException, JAXBException {
        recalboxDatabase.saveToFile(recalBoxDatabaseFile);
    }

    private void printStats() {
        System.out.println("Fixed game count: "+operationLogger.getFixedGameCount());
        System.out.println("Not fixed game count: "+operationLogger.getNotFixedGameCount());
        float percent = (float)(100*operationLogger.getFixedGameCount()) / (float)operationLogger.getTotalGameCount();
        System.out.println(String.format("%% of success : %.02f", percent));
        
        
        if( operationLogger.hasNotFixedGame()) {
            System.err.println("Cannot found the following games in Hyperspin database:");
            operationLogger.getNotFixedGames().stream().forEach(n -> System.err.println(n));
            System.err.println("-------------------------");            
        }
    }
}
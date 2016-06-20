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
        fixImage(recalboxGame);
        
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
    
    private void fixImage(RecalboxGame game) {
        String image = game.getImage();
        if( image == null) {
            operationLogger.logImageNotFound(game.getNameFromPath());
        } else {
            String fixedImage = image.replace("-image.", ".");
            game.setImage(fixedImage);
        }
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
        System.out.println("Games without images: "+ operationLogger.getNoImageGameCount());
        
        
        if( operationLogger.hasNotFixedGame()) {
            System.err.println("Cannot found the following games in Hyperspin database:");
            operationLogger.getNotFixedGames().stream().forEach(n -> System.err.println(n));
            System.err.println("-------------------------");            
        }
        
        if( operationLogger.hasNoImageGames()) {
            System.err.println("The following games have no images:");
            operationLogger.getNoImageGames().stream().forEach(n -> System.err.println(n));
            System.err.println("-------------------------");            
        }
    }
}
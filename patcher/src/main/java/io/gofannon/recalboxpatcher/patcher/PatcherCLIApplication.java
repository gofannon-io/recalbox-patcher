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
package io.gofannon.recalboxpatcher.patcher;

import io.gofannon.recalboxpatcher.patcher.patch.common.ChangeDirectoryPathPatcher;
import io.gofannon.recalboxpatcher.patcher.patch.common.PathPatcher;
import io.gofannon.recalboxpatcher.patcher.patch.database.DefaultGameDatabasePatcher;
import io.gofannon.recalboxpatcher.patcher.processor.FileResourceProvider;
import io.gofannon.recalboxpatcher.patcher.processor.GameDatabasePatchProcessor;
import io.gofannon.recalboxpatcher.patcher.processor.PatchProcessingResult;

import java.io.File;
import static org.apache.commons.lang3.Validate.*;


/**
 * Simple command line application for patching a recalbox file
 */
public class PatcherCLIApplication {
//TODO test this with real data files

    private File sourceRecalboxFile;
    private File hyperspinFile;
    private File fixedRecalboxFile;
    private File targetImageDirectory;
    private PathPatcher imagePatchPatcher;
    private GameDatabasePatchProcessor processor;
    private ApplicationHandler applicationHandler = new NullApplicationHandler();


    public static void main(String... args ) {
        run(new SystemApplicationHandler(), args);
    }

    public static void run(ApplicationHandler applicationHandler, String... args) {
        try {

            PatcherCLIApplication application = new PatcherCLIApplication();
            application.setApplicationHandler(applicationHandler);
            application.setArguments(args);
            application.initialize();
            application.process();
            applicationHandler.exitApplication();

        } catch( ApplicationInterruptedException ex ) {
            applicationHandler.exitApplicationOnFailure(ex);
        }
    }

    private void setApplicationHandler(ApplicationHandler applicationHandler) {
        notNull(applicationHandler, "applicationHandler is not defined");
        this.applicationHandler = applicationHandler;
    }

    private void setArguments(String... args) {
        // Check args
        if( args.length != 4) {
            applicationHandler.invalidArgumentCount();
        }

        sourceRecalboxFile = new File(args[0]);
        checkReadableFile( sourceRecalboxFile, "<original recalbox file>");

        hyperspinFile = new File(args[1]);
        checkReadableFile( hyperspinFile, "<hyperspin file>");

        fixedRecalboxFile = new File(args[2]);
        checkGenerableFile( fixedRecalboxFile, "<target recalbox file>");

//        targetImageDirectory = new File(args[3]);
//        checkWritableDirectory(targetImageDirectory, "<target image directory>");

        imagePatchPatcher = new ChangeDirectoryPathPatcher(args[3]);
    }


    private void checkReadableFile(File file, String documentedName) {
        if( file.exists()==false) {
            applicationHandler.argumentFileNotExists(documentedName);
        }
        if( file.isFile()==false) {
            applicationHandler.argumentFileNotAFile(documentedName);
        }
        if( file.canRead()==false) {
            applicationHandler.argumentFileNotReadable(documentedName);
        }
    }

    private void checkWritableDirectory(File directory, String documentedName) {
        if( directory.exists()==false) {
            applicationHandler.argumentDirectoryNotExist(documentedName);
        }

        if( directory.isDirectory()==false) {
            applicationHandler.argumentDirectoryNotADirectory(documentedName);
        }

        if( directory.canWrite()==false) {
            applicationHandler.argumentDirectoryNotWritable(documentedName);
        }
    }

    private void checkGenerableFile(File file, String documentedName) {
        if( file.exists()==true) {
            applicationHandler.argumentGenerableFileNotExists(documentedName);
        }

        File parentFile = file.getParentFile();

        if( parentFile.exists()==false) {
            applicationHandler.argumentGenerableFileParentDirectoryNotExist(documentedName);
        }

        if( parentFile.canWrite()==false) {
            applicationHandler.argumentGenerableFileParentDirectoryNotWritable(documentedName);
        }
    }

    private void initialize() {
        FileResourceProvider resourceProvider = new FileResourceProvider();
        resourceProvider.setInputRecalboxDatabaseFile(sourceRecalboxFile);
        resourceProvider.setInputHyperspinDatabaseFile(hyperspinFile);
        resourceProvider.setOutputRecalboxDatabaseFile(fixedRecalboxFile);
        resourceProvider.setImagePathPatcher(imagePatchPatcher);

        DefaultGameDatabasePatcher gamePatcher = new DefaultGameDatabasePatcher();
        gamePatcher.setImagePathPatcher(imagePatchPatcher);

        processor = new GameDatabasePatchProcessor();
        processor.setResourceProvider(resourceProvider);
        processor.setGamePatcher(gamePatcher);
    }

    private void process() {
        PatchProcessingResult result = processor.process();
        applicationHandler.displayResult(result);
    }

}
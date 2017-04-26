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

package io.gofannon.recalboxpatcher.patcher.processor;

import io.gofannon.recalboxpatcher.patcher.utils.IORuntimeException;
import io.gofannon.recalboxpatcher.patcher.patch.common.PathPatcher;
import io.gofannon.recalboxpatcher.patcher.model.hyperspin.FileHyperspinDatabase;
import io.gofannon.recalboxpatcher.patcher.model.hyperspin.HyperspinDatabase;
import io.gofannon.recalboxpatcher.patcher.model.recalbox.FileRecalboxDatabase;
import io.gofannon.recalboxpatcher.patcher.model.recalbox.RecalboxDatabase;
import io.gofannon.recalboxpatcher.patcher.model.recalbox.RecalboxGame;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.apache.commons.lang3.Validate.*;

/**
 * A {@link GameDatabasePatchResourceProvider} implementation based on file system.
 */
public class FileResourceProvider implements GameDatabasePatchResourceProvider {

    enum InitializationState {INITIAL, STARTUP, SHUTDOWN, STARTUP_FAILURE}

    private PathPatcher imagePathPatcher;

    private File inputHyperspinDatabaseFile;

    private File inputRecalboxDatabaseFile;

    private File outputRecalboxDatabaseFile;

    private FileHyperspinDatabase inputHyperspinDatabase;
    private FileRecalboxDatabase inputRecalboxDatabase;
    private FileRecalboxDatabase outputRecalboxDatabase = new FileRecalboxDatabase();


    private InitializationState state = InitializationState.INITIAL;

    public void setImagePathPatcher(PathPatcher imagePathPatcher) {
        this.imagePathPatcher = imagePathPatcher;
    }

    public void setInputHyperspinDatabaseFile(File inputHyperspinDatabaseFile) {
        this.inputHyperspinDatabaseFile = inputHyperspinDatabaseFile;
    }

    public void setInputRecalboxDatabaseFile(File inputRecalboxDatabaseFile) {
        this.inputRecalboxDatabaseFile = inputRecalboxDatabaseFile;
    }

    public void setOutputRecalboxDatabaseFile(File outputRecalboxDatabaseFile) {
        this.outputRecalboxDatabaseFile = outputRecalboxDatabaseFile;
    }

    @Override
    public PathPatcher getImagePathPatcher() {
        return imagePathPatcher;
    }

    @Override
    public HyperspinDatabase getInputHyperspinDatabase() {
        return inputHyperspinDatabase;
    }

    private void loadHyperspinDatabase() {
        try {

            FileHyperspinDatabase database = new FileHyperspinDatabase();
            database.loadFromFile(inputHyperspinDatabaseFile);
            this.inputHyperspinDatabase = database;

        } catch (IOException ioEx) {
            throw new IORuntimeException("Fail to load Hyperspin file " + toFilePath(inputHyperspinDatabaseFile), ioEx);
        }
    }

    private static String toFilePath(File file) {
        return file == null ? "null" : file.getAbsolutePath();
    }

    @Override
    public void startup() {
        checkStateBeforeStartup();

        boolean failureDetected = true;
        try {

            checkInitialized();
            loadHyperspinDatabase();
            loadRecalboxDatabase();
            backupRecalboxFileIfNecessary();
            failureDetected = false;

        } finally {
            updateStateAfterStartup(failureDetected);
        }
    }

    private void checkStateBeforeStartup() {
        if (state != InitializationState.INITIAL
                && state != InitializationState.STARTUP_FAILURE)
            throw new IllegalStateException("Invalid state to startup: " + state);
    }

    private void checkInitialized() {
        notNull(inputRecalboxDatabaseFile, "inputRecalboxDatabaseFile shall not be null");
        notNull(outputRecalboxDatabaseFile, "outputRecalboxDatabaseFile shall not be null");
        notNull(inputHyperspinDatabaseFile, "inputHyperspinDatabaseFile shall not be null");
        notNull(imagePathPatcher, "imagePathPatcher shall not be null");
    }

    private void backupRecalboxFileIfNecessary() {
        if (isSingleRecalboxFile()) {
            backupRecalboxFile();
        }
    }

    private void backupRecalboxFile() {
        try {

            File backupFile = defineBackupFile(inputRecalboxDatabaseFile);
            FileUtils.copyFile(inputRecalboxDatabaseFile, backupFile);
            this.inputRecalboxDatabaseFile = backupFile;

        } catch (IOException ioEx) {
            throw new IORuntimeException("Fail to backup file", ioEx);
        }
    }

    private boolean isSingleRecalboxFile() {
        try {
            return inputRecalboxDatabaseFile.getCanonicalPath().equals(outputRecalboxDatabaseFile.getCanonicalPath());
        } catch (IOException ioEx) {
            throw new IORuntimeException("Cannot compare files", ioEx);
        }
    }

    private static File defineBackupFile(File file) {
        for (int counter = 0; counter < 999; counter++) {
            String filename = String.format("%s.%03d.backup", file.getName(), counter);
            File bckFile = new File(file.getParentFile(), filename);
            if (bckFile.exists() == false)
                return bckFile;
        }

        throw new TooManyBackupFileException("Cannot backup file " + file.getAbsolutePath() + ", too many backup file");
    }

    private void updateStateAfterStartup(boolean failureDetected) {
        this.state = failureDetected ? InitializationState.STARTUP_FAILURE : InitializationState.STARTUP;
    }

    @Override
    public void shutdown() {
        checkStateBeforeShutdown();
        saveFixedRecalBoxDatase();
        checkStateAfterShutdown();
    }

    private void checkStateBeforeShutdown() {
        if (state != InitializationState.STARTUP)
            throw new IllegalStateException("Cannot shutdown if not start-up (state=" + state + ")");
    }

    private void saveFixedRecalBoxDatase() {

        try (OutputStream out = openRecalboxDatabaseTargetFile()) {

            outputRecalboxDatabase.saveToStream(out);

        } catch (IOException | JAXBException ioEx) {
            throw new IORuntimeException("Fail to write Recalbox file " + toFilePath(outputRecalboxDatabaseFile), ioEx);
        }
    }

    private OutputStream openRecalboxDatabaseTargetFile() throws IOException {
        OutputStream out = FileUtils.openOutputStream(outputRecalboxDatabaseFile);
        return IOUtils.buffer(out);
    }


    private void checkStateAfterShutdown() {
        this.state = InitializationState.SHUTDOWN;
    }

    @Override
    public RecalboxDatabase getInputRecalBoxDatabase() {
        return inputRecalboxDatabase;
    }

    private void loadRecalboxDatabase() {
        try {

            FileRecalboxDatabase database = new FileRecalboxDatabase();
            database.loadFromFile(inputRecalboxDatabaseFile);
            this.inputRecalboxDatabase = database;

        } catch (IOException ioEx) {
            throw new IORuntimeException("Fail to load Recalbox file " + toFilePath(inputRecalboxDatabaseFile), ioEx);
        }
    }

    @Override
    public RecalboxDatabase getOutputRecalBoxDatabase() {
        return outputRecalboxDatabase;
    }

    @Override
    public RecalboxGame duplicateGame(RecalboxGame game) {
        return game == null ? null : new RecalboxGame(game);
    }
}

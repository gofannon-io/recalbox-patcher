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

import io.gofannon.recalboxpatcher.patcher.hyperspin.FileHyperspinDatabase;
import io.gofannon.recalboxpatcher.patcher.hyperspin.HyperspinDatabase;
import io.gofannon.recalboxpatcher.patcher.recalbox.FileRecalboxDatabase;
import io.gofannon.recalboxpatcher.patcher.recalbox.RecalboxDatabase;
import io.gofannon.recalboxpatcher.patcher.recalbox.RecalboxGame;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.apache.commons.lang3.Validate.*;

/**
 * A {@link PatchResourceProvider} implementation based on file system.
 */
public class FileResourceProvider implements PatchResourceProvider {

    enum InitializationState {INITIAL, STARTUP, SHUTDOWN, STARTUP_FAILURE}

    private File hyperspinDatabaseFile;
    private File recalboxDatabaseSourceFile;
    private File recalboxDatabaseTargetFile;
    private String targetImagePath;

    private FileHyperspinDatabase hyperspinDatabase;
    private FileRecalboxDatabase recalboxDatabase;
    private FileRecalboxDatabase patchedRecalboxDatabase = new FileRecalboxDatabase();


    private InitializationState state = InitializationState.INITIAL;


    public void setHyperspinDatabaseFile(File hyperspinDatabaseFile) {
        this.hyperspinDatabaseFile = hyperspinDatabaseFile;
    }

    public void setRecalboxDatabaseSourceFile(File recalboxDatabaseSourceFile) {
        this.recalboxDatabaseSourceFile = recalboxDatabaseSourceFile;
    }

    public void setRecalboxDatabaseTargetFile(File recalboxDatabaseTargetFile) {
        this.recalboxDatabaseTargetFile = recalboxDatabaseTargetFile;
    }


    @Override
    public HyperspinDatabase getHyperspinDatabase() {
        if (hyperspinDatabase == null) {
            hyperspinDatabase = loadHyperspinDatabase();
        }

        return hyperspinDatabase;
    }

    private FileHyperspinDatabase loadHyperspinDatabase() {
        try {

            FileHyperspinDatabase database = new FileHyperspinDatabase();
            database.loadFromFile(hyperspinDatabaseFile);
            return database;

        } catch (IOException ioEx) {
            throw new IORuntimeException("Fail to load Hyperspin file " + toFilePath(hyperspinDatabaseFile), ioEx);
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
        notNull(recalboxDatabaseSourceFile, "recalboxDatabaseSourceFile shall not be null");
        notNull(recalboxDatabaseTargetFile, "recalboxDatabaseTargetFile shall not be null");
        notNull(hyperspinDatabaseFile, "hyperspinDatabaseFile shall not be null");
        notEmpty(targetImagePath, "targetImagePath shall not be null or empty");
    }


    private void backupRecalboxFileIfNecessary() {
        if (isSingleRecalboxFile()) {
            backupRecalboxFile();
        }
    }

    private void backupRecalboxFile() {
        try {

            File backupFile = defineBackupFile(recalboxDatabaseSourceFile);
            FileUtils.copyFile(recalboxDatabaseSourceFile, backupFile);
            this.recalboxDatabaseSourceFile = backupFile;

        } catch (IOException ioEx) {
            throw new IORuntimeException("Fail to backup file", ioEx);
        }
    }

    private boolean isSingleRecalboxFile() {
        try {
            return recalboxDatabaseSourceFile.getCanonicalPath().equals(recalboxDatabaseTargetFile.getCanonicalPath());
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

            patchedRecalboxDatabase.saveToStream(out);

        } catch (IOException | JAXBException ioEx) {
            throw new IORuntimeException("Fail to write Recalbox file " + toFilePath(recalboxDatabaseTargetFile), ioEx);
        }
    }

    private OutputStream openRecalboxDatabaseTargetFile() throws IOException {
        OutputStream out = FileUtils.openOutputStream(recalboxDatabaseTargetFile);
        return IOUtils.buffer(out);
    }


    private void checkStateAfterShutdown() {
        this.state = InitializationState.SHUTDOWN;
    }

    @Override
    public RecalboxDatabase getRecalBoxDatabase() {
        if (recalboxDatabase == null) {
            recalboxDatabase = loadRecalboxDatabase();
        }
        return recalboxDatabase;
    }

    private FileRecalboxDatabase loadRecalboxDatabase() {
        try {

            FileRecalboxDatabase database = new FileRecalboxDatabase();
            database.loadFromFile(recalboxDatabaseSourceFile);
            return database;

        } catch (IOException ioEx) {
            throw new IORuntimeException("Fail to load Recalbox file " + toFilePath(recalboxDatabaseSourceFile), ioEx);
        }
    }

    @Override
    public String getTargetImagePath() {
        return targetImagePath;
    }

    public void setTargetImagePath(String targetImagePath) {
        this.targetImagePath = targetImagePath;
    }

    @Override
    public RecalboxDatabase getPatchedRecalBoxDatabase() {
        return patchedRecalboxDatabase;
    }

    @Override
    public RecalboxGame duplicateGame(RecalboxGame game) {
        return game == null ? null : new RecalboxGame(game);
    }
}

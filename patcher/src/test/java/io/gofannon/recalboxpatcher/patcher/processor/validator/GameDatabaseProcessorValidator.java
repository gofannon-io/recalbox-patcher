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

package io.gofannon.recalboxpatcher.patcher.processor.validator;

import io.gofannon.recalboxpatcher.patcher.model.recalbox.FileRecalboxDatabase;
import io.gofannon.recalboxpatcher.patcher.model.recalbox.RecalboxGame;
import io.gofannon.recalboxpatcher.patcher.patch.common.IdentityPathPatcher;
import io.gofannon.recalboxpatcher.patcher.patch.common.PathPatcher;
import io.gofannon.recalboxpatcher.patcher.patch.database.DefaultGameDatabasePatcher;
import io.gofannon.recalboxpatcher.patcher.processor.FileResourceProvider;
import io.gofannon.recalboxpatcher.patcher.processor.GameDatabasePatchProcessor;
import io.gofannon.recalboxpatcher.patcher.processor.PatchProcessingResult;
import org.apache.commons.lang3.ObjectUtils;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

import static io.gofannon.recalboxpatcher.patcher.TestResourceHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

public class GameDatabaseProcessorValidator {

    private File inputRecalboxFile;
    private File inputHyperspinFile;
    private File outputRecalboxFile;

    private GameDatabasePatchProcessor patchProcessor;

    private FileRecalboxDatabase expectedRecalboxDatabase;
    private FileRecalboxDatabase outputRecalboxDatabase;

    private GameDatabaseValidationReportImpl report;

    public GameDatabaseValidationReport validateFromResourceDirectory(File workDirectory, String resourceDirectoryPath) throws IOException {
        initializeReport();
        initializeFilePaths(workDirectory);
        copyResourcesToWorkingDirectory(resourceDirectoryPath);
        buildProcessor();

        runPatchProcessing();

        loadOutputRecalboxDatabase();
        loadExpectedRecalboxDatabase(resourceDirectoryPath);
        assertOutputAndExepectedRecalboxDatabasesAreEqual();

        return report;
    }


    private void initializeReport() {
        report = new GameDatabaseValidationReportImpl();
    }


    private void initializeFilePaths(File workDirectory) {
        inputRecalboxFile = new File(workDirectory, "initial-recalbox.xml");
        inputHyperspinFile = new File(workDirectory, "hyperspin.xml");
        outputRecalboxFile = new File(workDirectory, "actual-recalbox.xml");
    }


    private void copyResourcesToWorkingDirectory(String resourceDirectoryPath) throws IOException {
        copyResourceToFile(resourceDirectoryPath + "/initial-recalbox.xml", inputRecalboxFile);
        copyResourceToFile(resourceDirectoryPath + "/hyperspin.xml", inputHyperspinFile);
    }


    private PathPatcher createImagePatcher() {
        return new IdentityPathPatcher();
    }


    private FileResourceProvider createResourceProvider(PathPatcher imagePathPatcher) {
        FileResourceProvider provider = new FileResourceProvider();
        provider.setInputRecalboxDatabaseFile(inputRecalboxFile);
        provider.setInputHyperspinDatabaseFile(inputHyperspinFile);
        provider.setOutputRecalboxDatabaseFile(outputRecalboxFile);
        provider.setImagePathPatcher(imagePathPatcher);
        return provider;
    }


    private DefaultGameDatabasePatcher createPatcher(PathPatcher imagePathPatcher) {
        DefaultGameDatabasePatcher patcher = new DefaultGameDatabasePatcher();
        patcher.setImagePathPatcher(imagePathPatcher);
        return patcher;
    }


    private GameDatabasePatchProcessor createProcessor(FileResourceProvider resourceProvider, DefaultGameDatabasePatcher patcher) {
        GameDatabasePatchProcessor processor = new GameDatabasePatchProcessor();
        processor.setResourceProvider(resourceProvider);
        processor.setGamePatcher(patcher);
        return processor;
    }


    private void buildProcessor() {
        PathPatcher imagePathPatcher = createImagePatcher();
        FileResourceProvider resourceProvider = createResourceProvider(imagePathPatcher);
        DefaultGameDatabasePatcher patcher = createPatcher(imagePathPatcher);
        patchProcessor = createProcessor(resourceProvider, patcher);
    }


    private void runPatchProcessing() {
        PatchProcessingResult processingResult = patchProcessor.process();
        this.report.setProcessingResult(processingResult);
    }


    private void loadOutputRecalboxDatabase() throws IOException {
        outputRecalboxDatabase = new FileRecalboxDatabase();
        outputRecalboxDatabase.loadFromFile(outputRecalboxFile);
    }


    private void loadExpectedRecalboxDatabase(String resourceDirectoryPath) throws IOException {
        expectedRecalboxDatabase = new FileRecalboxDatabase();
        expectedRecalboxDatabase.loadFromStream(openResourceAsStream(resourceDirectoryPath + "/expected-recalbox.xml"));
    }


    private void assertOutputAndExepectedRecalboxDatabasesAreEqual() {
        assertThat(outputRecalboxDatabase.getAllGames())
                .containsExactlyInAnyOrder(expectedRecalboxDatabase.getAllGames().toArray(new RecalboxGame[0]));

        List<GameValidationReport> gameValidationReports = outputRecalboxDatabase.getAllGames().stream()
                .map(this::validateGame)
                .collect(Collectors.toList());
        this.report.setGameValidationReports(gameValidationReports);
    }

    private GameValidationReport validateGame(RecalboxGame actualGame) {
        RecalboxGame expectedGame = expectedRecalboxDatabase.findByUniqueName(actualGame.getUniqueName());
        assertThat(expectedGame).as("check game %s in expected-recalbox.xml",actualGame.getUniqueName()).isNotNull();
        GameValidator validator = new GameValidator();
        return validator.validate(expectedGame, actualGame);
    }

    class GameValidator {

        private RecalboxGame expectedGame;
        private RecalboxGame actualGame;

        private String fieldName;
        private Method getterMethod;

        private GameValidationReportImpl gameReport;

        private final String[] fieldList = {
                "uniqueName",
                "path",
                "name",
                "desc",
                "image",
                "rating",
                "releaseDate",
                "developer",
                "publisher",
                "genre",
                "playCount",
                "lastPlayed"
        };

        private Object expectedValue;
        private Object actualValue;

        GameValidationReport validate(RecalboxGame expectedGame, RecalboxGame actualGame) {
            this.expectedGame = expectedGame;
            this.actualGame = actualGame;
            this.gameReport = new GameValidationReportImpl(actualGame.getUniqueName());

            for (String field : fieldList) {
                this.fieldName = field;
                checkFieldHasSameValue();
            }

            return gameReport;
        }


        private void checkFieldHasSameValue() {
            try {

                computeGetterMethod();
                checkGetterReturnSameValue();

            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Cannot find getter for field '"+fieldName+"'",e);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException("Fail to access to getter of field '"+fieldName+"'",e);
            }
        }

        private void computeGetterMethod() throws NoSuchMethodException {
            String methodName = "get" + fieldName.toUpperCase().charAt(0) + fieldName.substring(1);
            getterMethod = RecalboxGame.class.getMethod(methodName);
        }

        private Object getFieldValue(Object object) throws InvocationTargetException, IllegalAccessException {
            return getterMethod.invoke(object);
        }

        private void checkGetterReturnSameValue() throws InvocationTargetException, IllegalAccessException {
            this.expectedValue = getFieldValue(expectedGame);
            this.actualValue = getFieldValue(actualGame);

            if (Objects.equals(expectedGame,actualGame)==false) {
                registerFailureField();
            }
        }

        private void registerFailureField() {
            FailedField failedField = new FailedFieldImpl(fieldName, expectedValue, actualValue);
            gameReport.addFailedFieldReport(failedField);

        }
    }
}

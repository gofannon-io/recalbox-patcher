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

package io.gofannon.recalboxpatcher.patcher.view.model;

import io.gofannon.recalboxpatcher.patcher.image.ImageFormat;
import io.gofannon.recalboxpatcher.patcher.view.processing.ProcessingState;
import io.gofannon.recalboxpatcher.patcher.view.processing.PatchTaskContext;
import io.gofannon.recalboxpatcher.patcher.view.processing.PatchTaskResult;
import io.gofannon.recalboxpatcher.patcher.view.processing.PatcherProcessingService;
import io.gofannon.recalboxpatcher.patcher.view.processing.RecalboxPatcherTask;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

public class DefaultUIModel implements UIModel {

    private File lastSelectedDirectory;

    private StringProperty inputRecalboxFile;
    private StringProperty inputHyperspinFile;
    private StringProperty outputRecalboxFile;

    private StringProperty inputImageDirectory;
    private StringProperty outputImageDirectory;
    private StringProperty outputImageRelativeDirectory;

    private IntegerProperty widthImage;
    private IntegerProperty heightImage;
    private StringProperty imageExtension;

    private List<String> imageFileExtensionList = new ArrayList<>();

    private BooleanProperty notFoundOption;
    private BooleanProperty uppercaseOption;
    private BooleanProperty newFileOption;
    private BooleanProperty addNameOption;

    private StringProperty operationLog;

    private PatcherProcessingService patcherProcessingService;

    private ObjectProperty<ProcessingState> processingState;


    public DefaultUIModel() {
        for(ImageFormat format : ImageFormat.values()) {
            imageFileExtensionList.add(format.getDefaultExtension());
        }


        lastSelectedDirectory = FileUtils.getUserDirectory();

        inputRecalboxFile = new SimpleStringProperty(null, "inputRecalboxFile", null);
        inputRecalboxFile.addListener(this::onFileSelected);

        inputHyperspinFile = new SimpleStringProperty(null, "inputHyperspinFile", null);
        inputHyperspinFile.addListener(this::onFileSelected);

        outputRecalboxFile = new SimpleStringProperty(null, "outputRecalboxFile", null);
        outputRecalboxFile.addListener(this::onFileSelected);
        outputRecalboxFile.addListener(this::computeOutputImageRelativeDirectory);

        inputImageDirectory = new SimpleStringProperty(null, "inputImageDirectory", null);
        inputImageDirectory.addListener(this::onDirectorySelected);

        outputImageDirectory = new SimpleStringProperty(null, "outputImageDirectory", null);
        outputImageDirectory.addListener(this::onDirectorySelected);
        outputImageDirectory.addListener(this::computeOutputImageRelativeDirectory);

        outputImageRelativeDirectory = new SimpleStringProperty(null, "outputImageRelativeDirectory", null);

        widthImage = new SimpleIntegerProperty(null, "imageWidth", 100);
        heightImage = new SimpleIntegerProperty(null, "imageHeight", 100);
        imageExtension = new SimpleStringProperty(null, "imageExtension", ImageFormat.JPEG.getDefaultExtension());

        notFoundOption = new SimpleBooleanProperty(null, "notFoundOption", false);
        uppercaseOption = new SimpleBooleanProperty(null, "uppercaseOption", false);
        newFileOption = new SimpleBooleanProperty(null, "newFileOption", false);
        addNameOption = new SimpleBooleanProperty(null, "addNameOption", true);

        operationLog = new SimpleStringProperty(null, "operationsLog", null);

        processingState = new SimpleObjectProperty<>(null, "processingState", ProcessingState.INITIAL);

        patcherProcessingService = new PatcherProcessingService(RecalboxPatcherTask.class);
        patcherProcessingService.setOnSucceeded(this::processSucceeded);
        patcherProcessingService.setOnFailed(this::processOnFailed);
        patcherProcessingService.setOnCancelled(this::processOnCancelled);
    }

    @SuppressWarnings("unused")
    private void computeOutputImageRelativeDirectory(Observable observable) {
        String relativePath = computeOutputImageRelativeDirectory();
        outputImageRelativeDirectory.setValue(relativePath);
    }

    private String computeOutputImageRelativeDirectory() {
        File recalboxFile = getOutputRecalboxFile();
        File imageDirectory = getOutputImageDirectory();

        if (imageDirectory == null)
            return "";

        if (recalboxFile == null)
            return imageDirectory.getAbsolutePath();


        Path recalboxDirectoryPath = Paths.get(recalboxFile.getAbsoluteFile().getParent());
        Path imageDirectoryPath = Paths.get(imageDirectory.getAbsolutePath());
        Path relative = recalboxDirectoryPath.relativize(imageDirectoryPath);
        String asString = relative.toString();
        return startWithPathSeparator(asString) ? asString : "./" + asString;
    }

    private boolean startWithPathSeparator(String path) {
        return path.startsWith("/") || path.startsWith("./") || path.startsWith("../");
    }

    @Override
    public StringProperty inputRecalboxFileProperty() {
        return inputRecalboxFile;
    }

    @Override
    public StringProperty inputHyperspinFileProperty() {
        return inputHyperspinFile;
    }

    @Override
    public StringProperty outputRecalboxFileProperty() {
        return outputRecalboxFile;
    }

    @Override
    public StringProperty inputImageDirectoryProperty() {
        return inputImageDirectory;
    }

    @Override
    public StringProperty outputImageDirectoryProperty() {
        return outputImageDirectory;
    }

    @Override
    public StringProperty outputImageRelativeDirectoryProperty() {
        return outputImageRelativeDirectory;
    }

    @Override
    public IntegerProperty widthImageProperty() {
        return widthImage;
    }

    @Override
    public IntegerProperty heightImageProperty() {
        return heightImage;
    }

    @Override
    public File getInputRecalboxFile() {
        return toFile(inputRecalboxFile);
    }

    private static File toFile(StringProperty property) {
        return property.getValue() == null ? null : new File(property.getValue());
    }

    @Override
    public File getInputHyperspinFile() {
        return toFile(inputHyperspinFile);
    }

    @Override
    public File getOutputRecalboxFile() {
        return toFile(outputRecalboxFile);
    }

    @Override
    public File getInputImageDirectory() {
        return toFile(inputImageDirectory);
    }

    @Override
    public File getOutputImageDirectory() {
        return toFile(outputImageDirectory);
    }

    @Override
    public StringProperty imageExtensionProperty() {
        return imageExtension;
    }

    @Override
    public String[] getImageFileExtensionList() {
        return imageFileExtensionList.toArray(new String[0]);
    }

    @Override
    public BooleanProperty notFoundOptionProperty() {
        return notFoundOption;
    }

    @Override
    public BooleanProperty uppercaseOptionProperty() {
        return uppercaseOption;
    }

    @Override
    public BooleanProperty newFileOptionProperty() {
        return newFileOption;
    }

    @Override
    public BooleanProperty addNameOptionProperty() {
        return addNameOption;
    }

    @Override
    public StringProperty operationLogProperty() {
        return operationLog;
    }

    @Override
    public String getOperationLog() {
        return operationLog.getValue();
    }

    @Override
    public ProcessingState getProcessingState() {
        return processingStateProperty().getValue();
    }

    @Override
    public ObjectProperty<ProcessingState> processingStateProperty() {
        return processingState;
    }

    public void launchPatchProcess() {
        patcherProcessingService.setContext(createPatchTaskContext());

//        if( patcherProcessingService.getState()!= Worker.State.READY)
//            patcherProcessingService.reset();
        patcherProcessingService.restart();

        processingStateProperty().setValue(ProcessingState.RUNNING);
    }

    private PatchTaskContext createPatchTaskContext() {
        PatchTaskContext context = new PatchTaskContext();

        context.setInputRecalboxFile(getInputRecalboxFile());
        context.setInputHyperspinFile(getInputHyperspinFile());
        context.setOutputRecalboxFile(getOutputRecalboxFile());

        context.setInputImageDirectory(getInputImageDirectory());
        context.setOutputImageDirectory(getOutputImageDirectory());
        context.setOutputRelativeImageDirectory(outputImageRelativeDirectoryProperty().getValue());

        context.setWidthImage(widthImageProperty().getValue());
        context.setHeightImage(heightImageProperty().getValue());

        String imageExtension = imageExtensionProperty().getValue();
        ImageFormat imageFormat = ImageFormat.fromExtension(imageExtension);
        context.setImageFormat(imageFormat);

        context.setAddNameOption(addNameOptionProperty().getValue());
        context.setUppercaseOption(uppercaseOptionProperty().getValue());
        context.setNewFileOption(newFileOptionProperty().getValue());
        context.setNotFoundOption(notFoundOptionProperty().getValue());

        return context;
    }


    private void processSucceeded(WorkerStateEvent stateEvent) {
        updateLogFromPatcherProcessing();
        processingStateProperty().setValue(ProcessingState.SUCCESS);
    }

    private void updateLogFromPatcherProcessing() {
        PatchTaskResult patchTaskResult = patcherProcessingService.getValue();
        List<String> lines = patchTaskResult.getLogs();
        String[] linesAsArray = lines.toArray(new String[0]);
        String buffer = String.join("\n", linesAsArray);
        operationLog.setValue(buffer);
    }


    private void processOnFailed(WorkerStateEvent stateEvent) {
        operationLog.setValue("Failure");
        processingStateProperty().setValue(ProcessingState.FAILURE);
    }

    private void processOnCancelled(WorkerStateEvent stateEvent) {
        operationLog.setValue("Cancelled");
        processingStateProperty().setValue(ProcessingState.CANCEL);
    }

    @Override
    public PatchTaskResult geLastPatchResult() {
        return patcherProcessingService.getValue();
    }


    @Override
    public File getDefaultSelectionDirectory() {
        return lastSelectedDirectory;
    }


    private void onFileSelected(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        setDefaultDirectory(newValue);
    }

    private void setDefaultDirectory(String pathAsString) {
        if(isEmpty(pathAsString))
            return;

        try {
            File path = new File(pathAsString);
            this.lastSelectedDirectory = path.isDirectory() ? path : path.getParentFile();
        } catch(RuntimeException ex) {
            //Nothing to do, the lastSelectedDirectory shall not be changed
        }
    }

    private void onDirectorySelected(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        setDefaultDirectory(newValue);
    }
}
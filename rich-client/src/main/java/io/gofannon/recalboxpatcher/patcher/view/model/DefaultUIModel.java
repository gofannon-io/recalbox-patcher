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

import javafx.beans.Observable;
import javafx.beans.property.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DefaultUIModel implements UIModel {

    private StringProperty inputRecalboxFile;
    private StringProperty inputHyperspinFile;
    private StringProperty outputRecalboxFile;

    private StringProperty inputImageDirectory;
    private StringProperty outputImageDirectory;
    private StringProperty outputImageRelativeDirectory;

    private IntegerProperty widthImage;
    private IntegerProperty heightImage;
    private StringProperty imageExtension;
    private final String[] imageFileExtensionList = {"jpg", "png", "gif"};

    private BooleanProperty notFoundOption;
    private BooleanProperty upperCaseOption;
    private BooleanProperty newFileOption;
    private BooleanProperty addNameOption;

    private StringProperty operationLog;

    public DefaultUIModel() {
        inputRecalboxFile = new SimpleStringProperty(null,"inputRecalboxFile", null);
        inputHyperspinFile = new SimpleStringProperty(null,"inputHyperspinFile", null);
        outputRecalboxFile = new SimpleStringProperty(null,"outputRecalboxFile", null);
        outputRecalboxFile.addListener( this::computeOutputImageRelativeDirectory);

        inputImageDirectory = new SimpleStringProperty(null,"inputImageDirectory", null);
        outputImageDirectory = new SimpleStringProperty(null,"outputImageDirectory", null);
        outputImageRelativeDirectory = new SimpleStringProperty(null,"outputImageRelativeDirectory", null);
        outputImageDirectory.addListener( this::computeOutputImageRelativeDirectory);

        widthImage = new SimpleIntegerProperty(null, "imageWidth", 100);
        heightImage = new SimpleIntegerProperty(null, "imageHeight", 100);
        imageExtension = new SimpleStringProperty(null,"imageExtension", "jpg");

        notFoundOption = new SimpleBooleanProperty(null, "notFoundOption", false);
        upperCaseOption = new SimpleBooleanProperty(null, "upperCaseOption", false);
        newFileOption = new SimpleBooleanProperty(null, "newFileOption", false);
        addNameOption = new SimpleBooleanProperty(null, "addNameOption", true);

        operationLog = new SimpleStringProperty(null, "operationsLog", null);
    }

    private void computeOutputImageRelativeDirectory(Observable observable) {
        String relativePath = computeOutputImageRelativeDirectory();
        outputImageRelativeDirectory.setValue(relativePath);
    }

    private String computeOutputImageRelativeDirectory() {
        File recalboxFile = getOutputRecalboxFile();
        File imageDirectory = getOutputImageDirectory();

        if( imageDirectory == null )
            return "";

        if( recalboxFile == null )
            return imageDirectory.getAbsolutePath();


        Path recalboxDirectoryPath = Paths.get(recalboxFile.getAbsoluteFile().getParent());
        Path imageDirectoryPath = Paths.get(imageDirectory.getAbsolutePath());
        Path relative = recalboxDirectoryPath.relativize(imageDirectoryPath);
        String asString = relative.toString();
        return startWithPathSeparator(asString) ? asString : "./"+asString;
    }

    private boolean startWithPathSeparator(String path ) {
        return path.startsWith("/") || path.startsWith("./") || path.startsWith("../");
    }

    public StringProperty inputRecalboxFileProperty() {
        return inputRecalboxFile;
    }

    public StringProperty inputHyperspinFileProperty() {
        return inputHyperspinFile;
    }

    public StringProperty outputRecalboxFileProperty() {
        return outputRecalboxFile;
    }

    public StringProperty inputImageDirectoryProperty() {
        return inputImageDirectory;
    }

    public StringProperty outputImageDirectoryProperty() {
        return outputImageDirectory;
    }

    public StringProperty outputImageRelativeDirectoryProperty() {
        return outputImageRelativeDirectory;
    }

    public IntegerProperty widthImageProperty() {
        return widthImage;
    }

    public IntegerProperty heightImageProperty() {
        return heightImage;
    }

    public File getInputRecalboxFile() {
        return toFile(inputRecalboxFile);
    }

    private static File toFile(StringProperty property) {
        return property.getValue()== null ? null : new File(property.getValue());
    }

    public File getInputHyperspinFile() {
        return toFile(inputHyperspinFile);
    }

    public File getOutputRecalboxFile() {
        return toFile(outputRecalboxFile);
    }

    public File getInputImageDirectory() {
        return toFile(inputImageDirectory);
    }

    public File getOutputImageDirectory() {
        return toFile(outputImageDirectory);
    }

    public StringProperty imageExtensionProperty() {
        return imageExtension;
    }

    public String[] getImageFileExtensionList() {
        return imageFileExtensionList;
    }

    public BooleanProperty notFoundOptionProperty() {
        return notFoundOption;
    }

    public BooleanProperty upperCaseOptionProperty() {
        return upperCaseOption;
    }

    public BooleanProperty newFileOptionProperty() {
        return newFileOption;
    }

    public BooleanProperty addNameOptionProperty() {
        return addNameOption;
    }

    public StringProperty operationLogProperty() {
        return operationLog;
    }

    public void replaceLog(List<String> lines) {
        String buffer = String.join("\n", lines.toArray(new String[0]));
        operationLog.setValue(buffer);
    }
}
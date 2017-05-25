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

import javafx.beans.property.*;

import java.io.File;

public class UIModel {

    private StringProperty inputRecalboxFile;
    private StringProperty inputHyperspinFile;
    private StringProperty outputRecalboxFile;

    private StringProperty inputImageDirectory;
    private StringProperty outputImageDirectory;

    private IntegerProperty widthImage;
    private IntegerProperty heightImage;
    private StringProperty imageExtension;
    private final String[] imageFileExtensionList = {"jpg", "png", "gif"};

    public UIModel() {
        inputRecalboxFile = new SimpleStringProperty(null,"inputRecalboxFile", null);
        inputHyperspinFile = new SimpleStringProperty(null,"inputHyperspinFile", null);
        outputRecalboxFile = new SimpleStringProperty(null,"outputRecalboxFile", null);

        inputImageDirectory = new SimpleStringProperty(null,"inputImageDirectory", null);
        outputImageDirectory = new SimpleStringProperty(null,"outputImageDirectory", null);

        widthImage = new SimpleIntegerProperty(null, "imageWidth", 100);
        heightImage = new SimpleIntegerProperty(null, "imageHeight", 100);
        imageExtension = new SimpleStringProperty(null,"imageExtension", "jpg");
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
}

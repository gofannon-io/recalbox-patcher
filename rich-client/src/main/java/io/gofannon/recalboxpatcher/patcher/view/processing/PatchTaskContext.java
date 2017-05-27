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

package io.gofannon.recalboxpatcher.patcher.view.processing;

import java.io.File;

/**
 * Created by gwen on 26/05/17.
 */
public class PatchTaskContext {

    private File inputRecalboxFile;
    private File inputHyperspinFile;
    private File outputRecalboxFile;

    private File inputImageDirectory;
    private String outputImageDirectory;

    private int heightImage;
    private int widthImage;

    private String imageExtension;

    private boolean notFoundOption;
    private boolean uppercaseOption;
    private boolean newFileOption;
    private boolean addNameOption;

    public File getInputRecalboxFile() {
        return inputRecalboxFile;
    }

    public void setInputRecalboxFile(File inputRecalboxFile) {
        this.inputRecalboxFile = inputRecalboxFile;
    }

    public File getInputHyperspinFile() {
        return inputHyperspinFile;
    }

    public void setInputHyperspinFile(File inputHyperspinFile) {
        this.inputHyperspinFile = inputHyperspinFile;
    }

    public File getOutputRecalboxFile() {
        return outputRecalboxFile;
    }

    public void setOutputRecalboxFile(File outputRecalboxFile) {
        this.outputRecalboxFile = outputRecalboxFile;
    }

    public File getInputImageDirectory() {
        return inputImageDirectory;
    }

    public void setInputImageDirectory(File inputImageDirectory) {
        this.inputImageDirectory = inputImageDirectory;
    }

    public String getOutputImageDirectory() {
        return outputImageDirectory;
    }

    public void setOutputImageDirectory(String outputImageDirectory) {
        this.outputImageDirectory = outputImageDirectory;
    }

    public int getHeightImage() {
        return heightImage;
    }

    public void setHeightImage(int heightImage) {
        this.heightImage = heightImage;
    }

    public int getWidthImage() {
        return widthImage;
    }

    public void setWidthImage(int widthImage) {
        this.widthImage = widthImage;
    }

    public String getImageExtension() {
        return imageExtension;
    }

    public void setImageExtension(String imageExtension) {
        this.imageExtension = imageExtension;
    }

    public boolean isNotFoundOption() {
        return notFoundOption;
    }

    public void setNotFoundOption(boolean notFoundOption) {
        this.notFoundOption = notFoundOption;
    }

    public boolean isUppercaseOption() {
        return uppercaseOption;
    }

    public void setUppercaseOption(boolean uppercaseOption) {
        this.uppercaseOption = uppercaseOption;
    }

    public boolean isNewFileOption() {
        return newFileOption;
    }

    public void setNewFileOption(boolean newFileOption) {
        this.newFileOption = newFileOption;
    }

    public boolean isAddNameOption() {
        return addNameOption;
    }

    public void setAddNameOption(boolean addNameOption) {
        this.addNameOption = addNameOption;
    }
}

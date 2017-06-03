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

package io.gofannon.recalboxpatcher.patcher.view;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.commons.io.FileUtils;

import java.io.File;

import static io.gofannon.recalboxpatcher.patcher.view.FileExtensionUtils.*;


class FileSelector {

    private Window ownerWindow;

    private FileChooser fileChooser = new FileChooser();
    private File initialDirectory;
    private File initialFile;
    private File selectedFile;
    private String initialFilename;
    private String fileChooserTitle;
    private FileChooser.ExtensionFilter singleFileFilter = allFilesExtension();


    public FileSelector(Window ownerWindow, File initialDirectory) {
        this.ownerWindow = ownerWindow;
        this.initialDirectory = initialDirectory;
    }

    public void setInitialFile(File initialFile ) {
        this.initialFile = initialFile;

        if( this.initialFile == null ) {
            this.initialFilename = null;
        } else {
            this.initialDirectory = this.initialFile.getParentFile();
            this.initialFilename = this.initialFile.getName();
        }
    }

    public void setInitialFilename(String initialFilename) {
        this.initialFilename = initialFilename;
    }

    public void setFileChooserTitle(String fileChooserTitle) {
        this.fileChooserTitle = fileChooserTitle;
    }

    public void setSingleFileChooserExtension(FileChooser.ExtensionFilter fileFilter) {
        this.singleFileFilter = fileFilter;
    }

    public void addFileChooserExtensions(FileChooser.ExtensionFilter... filters ) {
        fileChooser.getExtensionFilters().addAll(filters);
    }

    public void setFileChooserExtensions(FileChooser.ExtensionFilter... filters ) {
        fileChooser.getExtensionFilters().clear();
        addFileChooserExtensions(filters);
    }


    @FunctionalInterface
    private static interface FileChooserDisplay {
        File display(FileChooser fileChooser);
    }

    public File showOpenDialog() {
        return showDialog( this::openDialog );
    }

    private File showDialog(FileChooserDisplay displayOperation) {
        fileChooser.setTitle(fileChooserTitle);
        fileChooser.getExtensionFilters().add(singleFileFilter);

        if( initialDirectory != null)
            fileChooser.setInitialDirectory(initialDirectory);

        if( initialFilename != null)
            fileChooser.setInitialFileName(initialFilename);

        File resultFile = displayOperation.display(fileChooser);
        this.selectedFile = resultFile==null ? this.initialFile : resultFile;
        return this.selectedFile;
    }

    private File openDialog(FileChooser fileChooser ) {
        return fileChooser.showOpenDialog(ownerWindow);
    }

    public File showSaveDialog() {
        return showDialog( this::saveDialog );
    }

    private File saveDialog(FileChooser fileChooser ) {
        return fileChooser.showSaveDialog(ownerWindow);
    }

    public String getSelectedFileAsString() {
        return selectedFile == null ? null : selectedFile.getAbsolutePath();
    }

}
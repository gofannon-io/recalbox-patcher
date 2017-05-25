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

public class FileSelector {

    private Window ownerWindow;

    private FileChooser fileChooser = new FileChooser();
    private File selectedFile;
    private File initialDirectory;
    private String initialFilename;
    private String fileChooserTitle;
    private FileChooser.ExtensionFilter singleFileFilter = new FileChooser.ExtensionFilter("All files (*.*)", "*.*");


    public FileSelector(Window ownerWindow) {
        this.ownerWindow = ownerWindow;
        setDefaultInitialDirectory();
    }

    private void setDefaultInitialDirectory() {
        initialDirectory = FileUtils.getUserDirectory();
    }

    public void setCurrentFile( File selectedFile ) {
        if( selectedFile == null ) {
            setDefaultInitialDirectory();
            this.initialFilename = null;
        } else {
            this.initialDirectory = selectedFile.getParentFile();
            this.initialFilename = selectedFile.getName();
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
        //fileChooser.setSelectedExtensionFilter(singleFileFilter);

        if( initialDirectory != null)
            fileChooser.setInitialDirectory(initialDirectory);

        if( initialFilename != null)
            fileChooser.setInitialFileName(initialFilename);

        this.selectedFile = displayOperation.display(fileChooser);
        return selectedFile;
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
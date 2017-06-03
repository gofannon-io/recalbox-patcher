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


import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class DirectorySelector {

    private Window ownerWindow;

    private DirectoryChooser directoryChooser = new DirectoryChooser();
    private File selectedDirectory;
    private String fileChooserTitle;
    private File initialDirectory;
    private File defaultDirectory;


    public DirectorySelector(Window ownerWindow) {
        this.ownerWindow = ownerWindow;
    }

    public void setInitialDirectory(File initialDirectory, File defaultDirectory ) {
        this.initialDirectory = initialDirectory;
        this.defaultDirectory = defaultDirectory;
    }

    public void setFileChooserTitle(String fileChooserTitle) {
        this.fileChooserTitle = fileChooserTitle;
    }

    public File showOpenDialog() {
        directoryChooser.setTitle(fileChooserTitle);
        directoryChooser.setInitialDirectory(this.initialDirectory == null ? this.defaultDirectory : this.initialDirectory);

        File resultDirectory = directoryChooser.showDialog(ownerWindow);
        this.selectedDirectory = resultDirectory == null ? this.initialDirectory : resultDirectory;
        return selectedDirectory;
    }

    public String getSelectedDirectoryAsString() {
        return selectedDirectory == null ? null : selectedDirectory.getAbsolutePath();
    }

}
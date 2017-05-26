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

import java.util.ResourceBundle;

/**
 * Created by gwen on 26/05/17.
 */
public class FileExtensionUtils {

    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("view");

    public static FileChooser.ExtensionFilter allFilesExtension() {
        return getExtensionFilter("FileExtensionUtils.allfiles.description", "*.*");
    }

    private static FileChooser.ExtensionFilter getExtensionFilter(String key, String... extensions) {
        String description = resourceBundle.getString(key);
        return new FileChooser.ExtensionFilter(description, extensions);
    }

    public static FileChooser.ExtensionFilter recalboxFilesExtension() {
        return getExtensionFilter("FileExtensionUtils.recalboxfiles.description", "*.xml");
    }

    public static FileChooser.ExtensionFilter hyperspinFilesExtension() {
        return getExtensionFilter("FileExtensionUtils.hyperspinfiles.description", "*.xml");
    }
}

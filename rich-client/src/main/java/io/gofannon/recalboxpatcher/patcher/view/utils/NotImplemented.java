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

package io.gofannon.recalboxpatcher.patcher.view.utils;

import javafx.scene.control.Alert;

public final class NotImplemented {

    private NotImplemented() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static void displayNotImplementedDialog() {
        Alert dialog = new Alert(Alert.AlertType.WARNING);
        dialog.setTitle("Warning");
        //dialog.setHeaderText("Functionality is not implemented");
        dialog.setContentText("This function is not implemented yet.");
        dialog.showAndWait();

    }

}

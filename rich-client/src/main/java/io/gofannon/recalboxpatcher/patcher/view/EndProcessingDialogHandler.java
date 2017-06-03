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


import io.gofannon.recalboxpatcher.patcher.view.model.UIModel;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

import java.util.ResourceBundle;


class EndProcessingDialogHandler {

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("view");

    private ProcessingStatePane processingStatePane;
    private TextArea logArea;
    private Dialog<Void> dialog;
    private UIModel model;

    public void initialize(Dialog<Void> dialog, UIModel model) {
        this.dialog = dialog;
        this.model = model;

        buildUI();
        fillUI();
    }

    private void buildUI() {
        processingStatePane = new ProcessingStatePane();


        logArea = new TextArea();
        logArea.setPrefRowCount(40);

        BorderPane rootPane = new BorderPane();
        rootPane.setTop(processingStatePane);
        rootPane.setCenter(logArea);


        dialog.getDialogPane().setContent(rootPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);


        final String title = resourceBundle.getString("endProcessingDialog.title");
        dialog.setTitle(title);

        dialog.setResizable(true);
    }

    private void fillUI() {
        processingStatePane.updateProcessingState(model.getProcessingState());
        logArea.setText(model.getOperationLog());
    }

}
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
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Window;

import java.util.ResourceBundle;

import static io.gofannon.recalboxpatcher.patcher.view.WidgetFactory.*;

class LogDialogHandler  {

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("view");

    private UIModel model;
    private Dialog<Void> dialog;

    private TextArea textArea;
    private Pane pane;


    public void initialize(Dialog<Void> dialog, UIModel model) {
        this.dialog = dialog;
        this.model = model;

        textArea = new TextArea();
        textArea.textProperty().bind(model.operationLogProperty());

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(textArea);


        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);


        this.pane = borderPane;
    }


    private void onClear(ActionEvent event) {

    }

    private void onClose(ActionEvent event) {
        System.out.println("Close");
    }

    public Pane getPane() {
        return pane;
    }


}

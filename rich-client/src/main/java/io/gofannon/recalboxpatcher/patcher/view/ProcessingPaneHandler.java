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
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Window;

import java.util.ResourceBundle;

public class ProcessingPaneHandler implements PaneHandler {

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("view");

    private BorderPane pane;
    private Label label;

    private Color defaultPaneColor;

    @Override
    public void initialize(Window ownerWindow, UIModel model) {
        pane = new BorderPane();
        label = new Label();
        label.setStyle("-fx-pref-height: 30px");

        pane.setStyle("-fx-border-color: darkgrey");

        pane.setCenter(label);

        model.processingStateProperty().addListener(this::onProcessingStateChanged);
        switchToNoProcess();
    }


    private void onProcessingStateChanged(ObservableValue<? extends ProcessingState> observable, ProcessingState oldValue, ProcessingState newValue) {
        switch(newValue) {
            case INITIAL:
                switchToNoProcess();
                break;
            case RUNNING:
                switchToRunningProcess();
                break;
            case SUCESS:
                switchToLastProcessSucceeded();
                break;
            case FAILURE:
                switchToLastProcessFailed();
                break;
            case CANCEL:
                switchToLastProcessCancelled();
                break;
            default:
        }
    }

    private void switchToNoProcess() {
        switchState("processing-pane.label.none");
    }

    private void switchState(String labelKey) {
        String text = resourceBundle.getString(labelKey);
        label.setText(text);
    }


    private void switchToRunningProcess() {
        switchState("processing-pane.label.running");
        pane.setStyle("-fx-background-color: #4a82fe");
    }

    private void switchToLastProcessSucceeded() {
        switchState("processing-pane.label.success");
        pane.setStyle("-fx-background-color: rgba(67,180,64,0.78)");
    }

    private void switchToLastProcessFailed() {
        switchState("processing-pane.label.failure");
        pane.setStyle("-fx-background-color: red");
    }

    private void switchToLastProcessCancelled() {
        switchState("processing-pane.label.cancel");
        pane.setStyle("");
    }

    @Override
    public Pane getPane() {
        return pane;
    }
}

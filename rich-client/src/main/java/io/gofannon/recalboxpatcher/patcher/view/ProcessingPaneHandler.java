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
import io.gofannon.recalboxpatcher.patcher.view.processing.ProcessingState;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;
import javafx.stage.Window;

import java.util.ResourceBundle;


public class ProcessingPaneHandler implements PaneHandler {

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("view");

    private ProcessingStatePane pane = new ProcessingStatePane();

    @Override
    public void initialize(Window ownerWindow, UIModel model) {
        model.processingStateProperty().addListener(this::onProcessingStateChanged);
    }


    private void onProcessingStateChanged(ObservableValue<? extends ProcessingState> observable, ProcessingState oldValue, ProcessingState newValue) {
        pane.updateProcessingState(newValue);
    }


    @Override
    public Pane getPane() {
        return pane;
    }
}
